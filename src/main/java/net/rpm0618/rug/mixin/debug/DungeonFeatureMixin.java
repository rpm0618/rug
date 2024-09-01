package net.rpm0618.rug.mixin.debug;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DungeonFeature;
import net.rpm0618.rug.rules.Rules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(DungeonFeature.class)
public abstract class DungeonFeatureMixin {
	// Best place I could find to inject, need to shift to capture the value of the random call
	@Inject(method = "place", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1, shift = At.Shift.BY, by = 3))
	private void onDungeonAttempt(World world, Random random, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) int k, @Local(ordinal = 3) int l, @Local(ordinal = 6) int o) {
		if (Rules.debugDungeonPopulation) {
			int minX = k;
			int maxX = l;
			int minZ = -o - 1;
			int maxZ = o + 1;

			System.out.println("Dungeon Attempt: (" + pos.getX() + " " + pos.getY() + " " + pos.getZ() + ") [" + (pos.getX() >> 4) + " " + (pos.getZ() >> 4) + "] X Range: [" + minX + ", " + maxX + "] Z Range: [" + minZ + " " + maxZ + "]");
		}
	}
}
