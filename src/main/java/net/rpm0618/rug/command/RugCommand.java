package net.rpm0618.rug.command;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.Formatting;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.rpm0618.rug.Rug;
import net.rpm0618.rug.rules.Rule;
import net.rpm0618.rug.rules.RuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RugCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "rug";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "/rug [<list|Rule Name>] [<Rule Value>]";
	}

	private void displayRule(CommandSource source, Rule rule) {
		Text ruleText = new LiteralText(" - " + rule.name + " ");
		ruleText.getStyle().setColor(Formatting.WHITE);

		Text descText = new LiteralText(rule.description);
		descText.getStyle().setColor(Formatting.YELLOW);

		ruleText.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, descText));

		String value = rule.getValue();
		for (String option : rule.options) {
			Text optionText = new LiteralText("[" + option + "]");
			if (rule.isDefault()) {
				optionText.getStyle().setColor(Formatting.GRAY);
			} else if (option.equalsIgnoreCase(rule.defaultValue)) {
				optionText.getStyle().setColor(Formatting.YELLOW);
			} else {
				optionText.getStyle().setColor(Formatting.DARK_GREEN);
			}

			if (option.equalsIgnoreCase(rule.defaultValue)) {
				optionText.getStyle().setBold(true);
			} else if (option.equalsIgnoreCase(value)) {
				optionText.getStyle().setUnderlined(true);
			}

			ruleText.append(optionText);
			ruleText.append(" ");
		}

		source.sendMessage(ruleText);
	}

	private void displayInfo(CommandSource source) {
		Text title = new LiteralText("Current Rug Rules");
		title.getStyle().setBold(true);
		source.sendMessage(title);

		boolean allDefault = true;
		for (Rule rule : RuleManager.getInstance().getRules()) {
			if (rule.isDefault()) {
				continue;
			}
			allDefault = false;
			displayRule(source, rule);
		}
		if (allDefault) {
			Text emptyText = new LiteralText("No settings are changed from default");
			emptyText.getStyle().setColor(Formatting.GRAY);
			emptyText.getStyle().setItalic(true);
			source.sendMessage(emptyText);
		}

		source.sendMessage(new LiteralText("Rug Version: " + Rug.VERSION));
	}

	private void listRules(CommandSource source) {
		Text title = new LiteralText("Rug Rules");
		title.getStyle().setBold(true);
		source.sendMessage(title);

		for (Rule rule : RuleManager.getInstance().getRules()) {
			displayRule(source, rule);
		}
	}

	private void displayRuleDetail(CommandSource source, Rule rule) {
		Text title = new LiteralText(rule.name);
		title.getStyle().setBold(true);
		source.sendMessage(title);

		Text descText = new LiteralText(rule.description);
		source.sendMessage(descText);

		String value = rule.getValue();

		Text valueLabelText = new LiteralText("Current Value: ");
		Text valueText = new LiteralText(value);
		if (rule.isDefault()) {
			valueText.append(" (Default)");
		} else {
			valueText.append(" (Modified)");
		}
		valueText.getStyle().setBold(true);
		valueLabelText.append(valueText);
		source.sendMessage(valueLabelText);

		Text optionsLabelText = new LiteralText("Options: ");
		Text optionsText = new LiteralText("[ ");
		optionsText.getStyle().setColor(Formatting.YELLOW);
		for (String option : rule.options) {
			Text optionText = new LiteralText(option);
			if (option.equalsIgnoreCase(rule.getValue())) {
				optionText.getStyle().setBold(true);
				optionText.getStyle().setColor(Formatting.GREEN);
			} else {
				optionText.getStyle().setColor(Formatting.YELLOW);
			}
			if (option.equalsIgnoreCase(rule.defaultValue)) {
				optionText.getStyle().setUnderlined(true);
			}
			optionsText.append(optionText);
			optionsText.append(" ");
		}
		optionsText.append("]");

		optionsLabelText.append(optionsText);
		source.sendMessage(optionsLabelText);
	}

	@Override
	public void run(CommandSource source, String[] args) throws CommandException {
		if (args.length == 0) {
			displayInfo(source);
			return;
		}

		if (args[0].equalsIgnoreCase("list")) {
			listRules(source);
		} else {
			Rule rule = RuleManager.getInstance().getRule(args[0]);
			if (rule == null) {
				source.sendMessage(new LiteralText("Unknown Rule: " + args[0]));
				return;
			}

			if (args.length == 1) {
				displayRuleDetail(source, rule);
			} else if (args.length == 2) {
				rule.setValue(args[1]);
				source.sendMessage(new LiteralText(rule.name + " " + rule.getValue()));
			} else {
				source.sendMessage(new LiteralText(getUsage(source)));
			}
		}
	}

	@Override
	public List<String> getSuggestions(CommandSource source, String[] args, BlockPos pos) {
		if (args.length == 1) {
			List<String> ruleNames = RuleManager.getInstance().getRules().stream()
				.map(r -> r.name)
				.collect(Collectors.toList());
			ruleNames.add("list");
			return suggestMatching(args, ruleNames);
		}

		if (args.length == 2) {
			Rule rule = RuleManager.getInstance().getRule(args[0]);
			if (rule == null) {
				return new ArrayList<>();
			}
			return suggestMatching(args, rule.options);
		}

		return new ArrayList<>();
	}
}
