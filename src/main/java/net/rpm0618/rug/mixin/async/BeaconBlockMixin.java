package net.rpm0618.rug.mixin.async;

import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rpm0618.rug.rules.Rules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlock.class)
public abstract class BeaconBlockMixin {

	@Inject(method = "neighborChanged", at = @At("TAIL"))
	private void asyncBeacons(World world, BlockPos pos, BlockState state, Block neighborBlock, CallbackInfo ci) {
		if (Rules.asyncBeaconUpdates && world.hasNeighborSignal(pos)) {
			HttpUtil.DOWNLOAD_THREAD_FACTORY.submit(() -> {
				try {
					world.onBlockChanged(pos, state.getBlock());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println("Beacon thread exiting");
				}
			});
		}
	}
}
