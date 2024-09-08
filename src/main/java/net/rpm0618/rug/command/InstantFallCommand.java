package net.rpm0618.rug.command;

import net.minecraft.block.FallingBlock;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InstantFallCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "instantFall";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/instantFall [<true|false>]";
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		if (args.length < 1) {
			source.sendMessage(new LiteralText("Instant Fall: " + FallingBlock.fallImmediately));
			return;
		}

		if (args[0].equalsIgnoreCase("true")) {
			FallingBlock.fallImmediately = true;
			source.sendMessage(new LiteralText("Enabling Instant Fall"));
		} else if (args[0].equalsIgnoreCase("false")) {
			FallingBlock.fallImmediately = false;
			source.sendMessage(new LiteralText("Disabling Instant Fall"));
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
