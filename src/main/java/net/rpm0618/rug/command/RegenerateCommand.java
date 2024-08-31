package net.rpm0618.rug.command;

import net.minecraft.server.ChunkMap;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.InvalidNumberException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.rpm0618.rug.extenstions.regenerate.ChunkMapExtension;
import net.rpm0618.rug.mixin.accessors.ChunkMapHolderAccessor;
import net.rpm0618.rug.mixin.accessors.ServerChunkCacheAccessor;

import java.util.ArrayList;
import java.util.List;

public class RegenerateCommand extends AbstractRugCommand{
	@Override
	public String getName() {
		return "regenerate";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/regenerate [x z]";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		World world = source.getSourceWorld();
		BlockPos position = source.getSourceBlockPos();
		int chunkX = position.getX() >> 4;
		int chunkZ = position.getZ() >> 4;

		if (args.length == 1 || args.length > 2) {
			source.sendMessage(new LiteralText(getUsage(source)));
			return;
		}

		if (args.length == 2) {
			try {
				chunkX = (int) parseCoordinate(chunkX, args[0], false);
				chunkZ = (int) parseCoordinate(chunkZ, args[1], false);
			} catch (InvalidNumberException e) {
				source.sendMessage(new LiteralText(getUsage(source)));
				return;
			}
		}

		ServerChunkCache chunkSource = (ServerChunkCache) world.getChunkSource();
		ChunkSource generator = ((ServerChunkCacheAccessor) chunkSource).getGenerator();
		Long2ObjectHashMap<WorldChunk> hashMap = ((ServerChunkCacheAccessor)chunkSource).getChunksByPos();
		hashMap.remove(ChunkPos.toLong(chunkX, chunkZ));
		WorldChunk chunk = generator.getChunk(chunkX, chunkZ);
		hashMap.put(ChunkPos.toLong(chunkX, chunkZ), chunk);
		chunk.load();
		chunk.setTerrainPopulated(true);
		chunk.tick(false);
		ChunkMapExtension chunkMap = (ChunkMapExtension) ((ServerWorld) world).getChunkMap();
		ChunkMap.ChunkHolder holder = chunkMap.rug$GetChunkHolder(chunkX, chunkZ);
		if (holder != null) {
			ChunkMapHolderAccessor accessor = (ChunkMapHolderAccessor) holder;
			// Force all blocks to be sent
			accessor.setBlocksChanged(64);
			accessor.setDirtySections(0xffffffff);
			holder.sendChanges();
		}
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String[] args, BlockPos pos) {
		if (args.length <= 2) {
			return suggestChunkCoordinate(args, 0, source.getSourceBlockPos());
		}

		return new ArrayList<>();
	}
}
