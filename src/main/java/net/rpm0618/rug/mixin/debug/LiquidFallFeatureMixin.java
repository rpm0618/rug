package net.rpm0618.rug.mixin.debug;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.LiquidFallFeature;
import net.rpm0618.rug.rules.Rules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LiquidFallFeature.class)
public class LiquidFallFeatureMixin {
	@Inject(method = "place", at = @At(value = "HEAD"))
	private void onLiquidFallAttempt(World world, Random random, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (Rules.debugLiquidPopulation) {
			System.out.println("Liquid attempt: (" + pos.getX() + " " + pos.getY() + " " + pos.getZ() + ") in chunk [" + (pos.getX() >> 4) + " " + (pos.getZ() >> 4) + "]");
		}
	}
}
