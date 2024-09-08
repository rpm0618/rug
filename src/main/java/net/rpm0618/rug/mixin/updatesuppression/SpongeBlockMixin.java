package net.rpm0618.rug.mixin.updatesuppression;

import net.minecraft.block.Block;
import net.minecraft.block.SpongeBlock;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rpm0618.rug.rules.Rules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpongeBlock.class)
public abstract class SpongeBlockMixin {
	@Inject(method = "neighborChanged", at = @At("HEAD"))
	private void updateSuppress(World world, BlockPos pos, BlockState state, Block neighborBlock, CallbackInfo ci) {
		if (Rules.updateSuppressionSponge) {
			throw new RuntimeException("Rug Update Suppression");
		}
	}
}
