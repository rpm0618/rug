package net.rpm0618.rug.mixin.chunkdebug;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.rpm0618.rug.command.ChunkDebugCommand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherChunkGenerator.class)
public abstract class NetherChunkGeneratorMixin {
	@Final
	@Shadow
	private World world;

	@Inject(method = "populateChunk", at = @At(value = "HEAD"))
	private void onChunkPopulated(ChunkSource source, int chunkX, int chunkZ, CallbackInfo ci) {
		if (ChunkDebugCommand.chunkDebugEnabled) {
			ChunkDebugCommand.onChunkPopulated(chunkX, chunkZ, world, null);
		}
	}
}
