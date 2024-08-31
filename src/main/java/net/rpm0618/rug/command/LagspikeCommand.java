package net.rpm0618.rug.command;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;

public class LagspikeCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "lagspike";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/lagspike <durationMs>";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		if (args.length != 1) {
			source.sendMessage(new LiteralText(getUsage(source)));
		}

		try {
			int durationMs = Integer.parseInt(args[0]);
			Thread.sleep(durationMs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}
}
