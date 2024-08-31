package net.rpm0618.rug.mixin.accessors;

import net.minecraft.server.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkMap.ChunkHolder.class)
public interface ChunkMapHolderAccessor {
	@Accessor("blocksChanged")
	void setBlocksChanged(int blocksChanged);

	@Accessor("dirtySections")
	void setDirtySections(int dirtySections);
}
