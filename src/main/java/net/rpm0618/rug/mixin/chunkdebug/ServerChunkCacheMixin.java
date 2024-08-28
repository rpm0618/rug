package net.rpm0618.rug.mixin.chunkdebug;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.world.chunk.WorldChunk;
import net.rpm0618.rug.command.ChunkDebugCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {

	@Shadow
	private ServerWorld world;

	@Shadow
	private Long2ObjectHashMap<WorldChunk> chunksByPos;

	@Inject(method = "generateChunk", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/chunk/ServerChunkCache;loadChunk(II)Lnet/minecraft/world/chunk/WorldChunk;"))
	private void onChunkLoaded(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir, @Local WorldChunk chunk) {
		if (ChunkDebugCommand.chunkDebugEnabled && chunk != null) {
			ChunkDebugCommand.onChunkLoaded(chunkX, chunkZ, world, null);
		}
	}

	@Inject(method = "generateChunk", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/chunk/ChunkSource;getChunk(II)Lnet/minecraft/world/chunk/WorldChunk;"))
	private void onChunkGenerated(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir) {
		if (ChunkDebugCommand.chunkDebugEnabled) {
			ChunkDebugCommand.onChunkGenerated(chunkX, chunkZ, world, null);
		}
	}

	@Inject(method = "unloadChunk", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
	private void onChunkUnloadScheduled(int chunkX, int chunkZ, CallbackInfo ci) {
		if (ChunkDebugCommand.chunkDebugEnabled) {
			ChunkDebugCommand.onChunkUnloadScheduled(chunkX, chunkZ, world, null);
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"))
	private void onChunkUnloaded(CallbackInfoReturnable<Boolean> cir, @Local WorldChunk worldChunk) {
		if (ChunkDebugCommand.chunkDebugEnabled) {
			ChunkDebugCommand.onChunkUnloaded(worldChunk.chunkX, worldChunk.chunkZ, world, null);
		}
	}
}
