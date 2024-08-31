package net.rpm0618.rug.mixin.regenerate;

import net.minecraft.server.ChunkMap;
import net.minecraft.util.Long2ObjectHashMap;
import net.rpm0618.rug.extenstions.regenerate.ChunkMapExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public class ChunkMapMixin implements ChunkMapExtension {

	@Final
	@Shadow
	private Long2ObjectHashMap<ChunkMap.ChunkHolder> chunksByPos;

	@Override
	public ChunkMap.ChunkHolder rug$GetChunkHolder(int x, int z) {
		// For whatever reason, using @Invoke to call ChunkMap.getChunk to fetch the holder fails,
		// so we use mixin to accomplish the same thing
		long l = (long)x + 2147483647L | (long)z + 2147483647L << 32;
		return this.chunksByPos.get(l);
	}
}
