package net.rpm0618.rug.mixin;

import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.handler.CommandRegistry;
import net.rpm0618.rug.Rug;
import net.rpm0618.rug.command.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandMixin extends CommandRegistry {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerCommands(CallbackInfo ci) {
		this.register(new AutosaveCommand());
		this.register(new ChunkDebugCommand());
		this.register(new HashCommand());
		this.register(new InstantFallCommand());
		this.register(new InstantSchedulingCommand());
		this.register(new LagspikeCommand());
		this.register(new RegenerateCommand());
		this.register(new RepopulateCommand());
		this.register(new RugCommand());
	}
}
