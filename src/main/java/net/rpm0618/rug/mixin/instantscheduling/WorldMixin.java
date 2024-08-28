package net.rpm0618.rug.mixin.instantscheduling;

import net.minecraft.world.World;
import net.rpm0618.rug.helpers.InstantSchedulingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements InstantSchedulingHelper {
	@Shadow
	protected boolean doTicksImmediately;

	@Override
	public boolean rug$getInstantScheduling() {
		return doTicksImmediately;
	}

	@Override
	public void rug$setInstantScheduling(boolean value) {
		doTicksImmediately = value;
	}
}
