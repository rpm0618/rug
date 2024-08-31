package net.rpm0618.rug.command;

import com.google.common.collect.Lists;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.InvalidNumberException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.text.LiteralText;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.rpm0618.rug.mixin.accessors.ServerChunkCacheAccessor;

import java.util.ArrayList;
import java.util.List;

public class RepopulateCommand extends AbstractRugCommand{
	@Override
	public String getName() {
		return "repopulate";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/repopulate [x z] [<now|async>]";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		World world = source.getSourceWorld();
		BlockPos position = source.getSourceBlockPos();
		int chunkX = position.getX() >> 4;
		int chunkZ = position.getZ() >> 4;

		boolean now = false;
		boolean async = false;

		if (args.length == 1 || args.length > 3) {
			source.sendMessage(new LiteralText(getUsage(source)));
			return;
		}

		if (args.length >= 2) {
			try {
				chunkX = (int) parseCoordinate(chunkX, args[0], false);
				chunkZ = (int) parseCoordinate(chunkZ, args[1], false);
			} catch (InvalidNumberException e) {
				source.sendMessage(new LiteralText(getUsage(source)));
				return;
			}
		}

		if (args.length == 3) {
			if (args[2].equalsIgnoreCase("now")) {
				now = true;
			} else if (args[2].equalsIgnoreCase("async")) {
				now = true;
				async = true;
			} else {
				source.sendMessage(new LiteralText(getUsage(source)));
				return;
			}
		}

		if (now) {
			if (!isRepopAreaLoaded(world, chunkX, chunkZ)) {
				source.sendMessage(new LiteralText("Warning: Area not loaded for repopulation!"));
			}

			if (async) {
				int finalChunkX = chunkX;
				int finalChunkZ = chunkZ;
				HttpUtil.DOWNLOAD_THREAD_FACTORY.submit(() -> {
					try {
						repopulate(world, finalChunkX, finalChunkZ);
						System.out.println("Chunk async repopulation ended.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				repopulate(world, chunkX, chunkZ);
			}
		} else {
			WorldChunk chunk = world.getChunkAt(chunkX, chunkZ);
			chunk.setTerrainPopulated(false);
		}
	}

	private void repopulate(World world, int chunkX, int chunkZ) {
		ServerChunkCache chunkSource = (ServerChunkCache) world.getChunkSource();
		ChunkSource generator = ((ServerChunkCacheAccessor) chunkSource).getGenerator();
		WorldChunk chunk = chunkSource.getChunk(chunkX, chunkZ);
		chunk.setTerrainPopulated(false);
		chunk.populate(chunkSource, generator, chunkX, chunkZ);
	}

	private boolean isRepopAreaLoaded(World world, int chunkX, int chunkZ) {
		ServerChunkCache chunkSource = (ServerChunkCache) world.getChunkSource();
		Long2ObjectHashMap<WorldChunk> hashMap = ((ServerChunkCacheAccessor)chunkSource).getChunksByPos();
		return hashMap.contains(ChunkPos.toLong(chunkX, chunkZ))
			&& hashMap.contains(ChunkPos.toLong(chunkX + 1, chunkZ))
			&& hashMap.contains(ChunkPos.toLong(chunkX, chunkZ + 1))
			&& hashMap.contains(ChunkPos.toLong(chunkX + 1, chunkZ + 1));
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String[] args, BlockPos pos) {
		if (args.length <= 2) {
			return suggestChunkCoordinate(args, 0, source.getSourceBlockPos());
		}

		if (args.length == 3) {
			return Lists.newArrayList("async", "now");
		}

		return new ArrayList<>();
	}
}
