package net.rpm0618.rug.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;

public class AutosaveCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "as";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/as";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		MinecraftServer server = MinecraftServer.getInstance();
		int ticksToAutosave = 900 - (server.getTicks() % 900);
		source.sendMessage(new LiteralText("To Autosave: " + ticksToAutosave));
	}
}
