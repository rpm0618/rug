package net.rpm0618.rug.command;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.rpm0618.rug.mixin.accessors.WorldAccessor;

import java.util.List;

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
		WorldAccessor world = (WorldAccessor) source.asEntity().getSourceWorld();

		if (args.length < 1) {
			source.sendMessage(new LiteralText("Instant Scheduling: " + world.getDoTicksImmediately()));
			return;
		}

		if (args[0].equalsIgnoreCase("true")) {
			world.setDoTicksImmediately(true);
			source.sendMessage(new LiteralText("Enabling Instant Scheduling"));
		} else if (args[0].equalsIgnoreCase("false")) {
			world.setDoTicksImmediately(false);
			source.sendMessage(new LiteralText("Disabling Instant Scheduling"));
		} else {
			source.sendMessage(new LiteralText(getUsage(source)));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String[] args, BlockPos pos) {
		return suggestMatching(args, "true", "false");
	}
}
