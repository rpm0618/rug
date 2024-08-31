package net.rpm0618.rug.mixin.accessors;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(World.class)
public interface WorldAccessor {
	@Accessor("doTicksImmediately")
	boolean getDoTicksImmediately();

	@Accessor("doTicksImmediately")
	void setDoTicksImmediately(boolean doTicksImmediately);
}
