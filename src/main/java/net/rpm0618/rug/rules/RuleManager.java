package net.rpm0618.rug.rules;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleManager {
	private static RuleManager instance;

	private final Map<String, Rule> rules;

	public static RuleManager getInstance() {
		if (instance == null) {
			throw new RuntimeException("Call RuleManager.init() first!");
		}
		return instance;
	}

	private RuleManager() {
		rules = new HashMap<>();
	}

	public static void init() {
		instance = new RuleManager();
		try {
			Class<Rules> clazz = Rules.class;
			for (Field field : clazz.getFields()) {
				if (field.isAnnotationPresent(RugRule.class)) {
					String name = field.getName();
					RugRule rugRule = field.getAnnotation(RugRule.class);
					String defaultValue = String.valueOf(field.get(null));
					String[] options = field.getType() == boolean.class ? new String[]{"true", "false"} : rugRule.options();
					instance.rules.put(name, new Rule(name, rugRule.description(), defaultValue, options, field));
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Invalid private field on Rules class");
		}
	}

	public Rule getRule(String name) {
		return rules.get(name);
	}

	public Collection<Rule> getRules() {
		return rules.values();
	}
}
