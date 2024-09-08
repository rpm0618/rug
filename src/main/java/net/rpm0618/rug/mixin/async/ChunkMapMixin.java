package net.rpm0618.rug.mixin.async;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.ChunkMap;
import net.rpm0618.rug.rules.Rules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
	@Shadow
	@Final
	private List<ChunkMap.ChunkHolder> dirty;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ChunkMap$ChunkHolder;sendChanges()V"), cancellable = true)
	private void asyncCrashFix(CallbackInfo ci, @Local ChunkMap.ChunkHolder chunkHolder) {
		if (Rules.asyncCrashFix && chunkHolder == null) {
			dirty.clear();
			ci.cancel();
		}
	}
}
