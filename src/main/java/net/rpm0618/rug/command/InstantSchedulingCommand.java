package net.rpm0618.rug.command;

import net.minecraft.block.FallingBlock;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import net.rpm0618.rug.helpers.InstantSchedulingHelper;

public class InstantSchedulingCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "instantScheduling";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/instantScheduling [<true|false>]";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		InstantSchedulingHelper world = (InstantSchedulingHelper) source.asEntity().getSourceWorld();

		if (args.length < 1) {
			source.sendMessage(new LiteralText("Instant Scheduling: " + world.rug$getInstantScheduling()));
			return;
		}

		if (args[0].equalsIgnoreCase("true")) {
			world.rug$setInstantScheduling(true);
			source.sendMessage(new LiteralText("Enabling Instant Scheduling"));
		} else if (args[0].equalsIgnoreCase("false")) {
			world.rug$setInstantScheduling(false);
			source.sendMessage(new LiteralText("Disabling Instant Scheduling"));
		} else {
			source.sendMessage(new LiteralText(getUsage(source)));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
