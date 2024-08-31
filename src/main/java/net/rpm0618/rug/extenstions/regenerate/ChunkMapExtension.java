package net.rpm0618.rug.extenstions.regenerate;

import net.minecraft.server.ChunkMap;

public interface ChunkMapExtension {
	ChunkMap.ChunkHolder rug$GetChunkHolder(int x, int z);
}
