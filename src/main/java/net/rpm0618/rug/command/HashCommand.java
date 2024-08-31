package net.rpm0618.rug.command;

import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.InvalidNumberException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.rpm0618.rug.mixin.accessors.Long2ObjectHashMapAccessor;
import net.rpm0618.rug.mixin.accessors.Long2ObjectHashMapNodeAccessor;
import net.rpm0618.rug.mixin.accessors.ServerChunkCacheAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HashCommand extends AbstractRugCommand {
	@Override
	public String getName() {
		return "hash";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/hash <dump|info> [x] [z]";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		if (args.length == 0) {
			source.sendMessage(new LiteralText(getUsage(source)));
		}

		if (args[0].equalsIgnoreCase("dump")) {
			dump(source);
		} else if (args[0].equalsIgnoreCase("info")) {
			info(source, args);
		}
	}

	private void dump(@NotNull CommandSource source) throws CommandException {
		ServerChunkCache chunkSource = (ServerChunkCache) source.getSourceWorld().getChunkSource();
		Long2ObjectHashMap<WorldChunk> hashMap = ((ServerChunkCacheAccessor)chunkSource).getChunksByPos();
		Long2ObjectHashMapAccessor<WorldChunk> accessor = (Long2ObjectHashMapAccessor<WorldChunk>) hashMap;

		source.sendMessage(new LiteralText("Chunks: " + hashMap.size()));
		source.sendMessage(new LiteralText("Mask: " + accessor.getMask()));

		String fileName = "hashmap-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date()) + ".csv";
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(fileName));

			Long2ObjectHashMap.Node<WorldChunk>[] nodes = accessor.getNodes();

			for (int i = 0; i < nodes.length; i++) {
				Long2ObjectHashMap.Node<WorldChunk> node = nodes[i];
				pw.println(i + ": " + getBucketString(node));
			}

			pw.flush();
			pw.close();

			source.sendMessage(new LiteralText("Writing to file: " + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

	private String getBucketString(Long2ObjectHashMap.Node<WorldChunk> node) {
		if (node == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		Long2ObjectHashMap.Node<WorldChunk> currNode = node;
		while (currNode != null) {
			WorldChunk chunk = currNode.getValue();

			pw.print("(" + chunk.chunkX + "," + chunk.chunkZ + "),");
			currNode = ((Long2ObjectHashMapNodeAccessor<WorldChunk>) currNode).getNext();
		}

		return sw.toString();
	}

	private void info(@NotNull CommandSource source, String[] args) {
		ServerChunkCache chunkSource = (ServerChunkCache) source.getSourceWorld().getChunkSource();
		Long2ObjectHashMap<WorldChunk> hashMap = ((ServerChunkCacheAccessor)chunkSource).getChunksByPos();
		Long2ObjectHashMapAccessor<WorldChunk> accessor = (Long2ObjectHashMapAccessor<WorldChunk>) hashMap;

		BlockPos position = source.getSourceBlockPos();
		int chunkX = position.getX() >> 4;
		int chunkZ = position.getZ() >> 4;

		if (args.length == 3) {
			try {
				chunkX = (int) parseCoordinate(chunkX, args[1], false);
				chunkZ = (int) parseCoordinate(chunkZ, args[2], false);
			} catch (InvalidNumberException e) {
				source.sendMessage(new LiteralText(getUsage(source)));
				return;
			}
		} else if (args.length != 1) {
			source.sendMessage(new LiteralText(getUsage(source)));
			return;
		}

		long chunkLong = ChunkPos.toLong(chunkX, chunkZ);
		int chunkHash = Long2ObjectHashMapAccessor.hash(chunkLong);
		int chunkIndex = Long2ObjectHashMapAccessor.index(chunkHash, accessor.getMask());

		source.sendMessage(new LiteralText("Chunk: (" + chunkX + ", " + chunkZ + ")"));
		source.sendMessage(new LiteralText("Chunk Long: " + chunkLong + " Chunk Hash: " + chunkHash));
		source.sendMessage(new LiteralText("Chunk Hash Index: " + chunkIndex));
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return suggestMatching(args, "dump", "info");
		}

		if (args[0].equalsIgnoreCase("info")) {
			return suggestChunkCoordinate(args, 1, source.getSourceBlockPos());
		}

		return new ArrayList<>();
	}
}
