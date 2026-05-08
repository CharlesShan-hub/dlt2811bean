package com.ysh.dlt2811bean.cli;

import java.util.List;

public class Param {
    private final String name;
    private final String prompt;
    private final String defaultValue;
    private final boolean required;
    private final List<EnumChoice> enumChoices;

    public static class EnumChoice {
        public final String value;
        public final String label;
        public EnumChoice(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    public Param(String name, String prompt, String defaultValue) {
        this(name, prompt, defaultValue, false, List.of());
    }

    public Param(String name, String prompt, String defaultValue, boolean required) {
        this(name, prompt, defaultValue, required, List.of());
    }

    public Param(String name, String prompt, String defaultValue, List<EnumChoice> enumChoices) {
        this(name, prompt, defaultValue, false, enumChoices);
    }

    public Param(String name, String prompt, String defaultValue, boolean required, List<EnumChoice> enumChoices) {
        this.name = name;
        this.prompt = prompt;
        this.defaultValue = defaultValue;
        this.required = required;
        this.enumChoices = enumChoices;
    }

    public String getName() { return name; }
    public String getPrompt() { return prompt; }
    public String getDefaultValue() { return defaultValue; }
    public boolean isRequired() { return required; }
    public List<EnumChoice> getEnumChoices() { return enumChoices; }
}