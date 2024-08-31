package net.rpm0618.rug.rules;

import java.lang.reflect.Field;

public class Rule {
	public String name;
	public String description;
	public String defaultValue;
	public String[] options;
	public Field field;

	public Rule(String name, String description, String defaultValue, String[] options, Field field) {
		this.name = name;
		this.description = description;
		this.defaultValue = defaultValue;
		this.options = options;
		this.field = field;
	}

	public void setValue(String value) {
		Class<?> fieldType = this.field.getType();

		try {
			Object convertedValue;
			if (fieldType == int.class) {
				convertedValue = Integer.parseInt(value);
			} else if (fieldType == long.class) {
				convertedValue = Long.parseLong(value);
			} else if (fieldType == double.class) {
				convertedValue = Double.parseDouble(value);
			} else if (fieldType == boolean.class) {
				convertedValue = Boolean.parseBoolean(value);
			} else if (fieldType == String.class) {
				convertedValue = value;
			} else {
				throw new RuntimeException("Unexpected type for rule " + this.name);
			}
			this.field.set(null, convertedValue); // Assuming static field, otherwise use a specific object reference
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public String getValue() {
		try {
			return String.valueOf(this.field.get(null));
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Invalid private field on Rules class");
		}
	}

	public boolean isDefault() {
		return this.getValue().equalsIgnoreCase(this.defaultValue);
	}
}
