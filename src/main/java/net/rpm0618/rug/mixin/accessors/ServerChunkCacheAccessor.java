package net.rpm0618.rug.mixin.accessors;

import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkCache.class)
public interface ServerChunkCacheAccessor {
	@Accessor("chunksByPos")
	Long2ObjectHashMap<WorldChunk> getChunksByPos();

	@Accessor("generator")
	ChunkSource getGenerator();
}
