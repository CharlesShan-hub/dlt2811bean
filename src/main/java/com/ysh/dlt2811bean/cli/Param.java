package com.ysh.dlt2811bean.cli;

public class Param {
    private final String name;
    private final String prompt;
    private final String defaultValue;
    private final boolean required;

    public Param(String name, String prompt, String defaultValue) {
        this(name, prompt, defaultValue, false);
    }

    public Param(String name, String prompt, String defaultValue, boolean required) {
        this.name = name;
        this.prompt = prompt;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getName() { return name; }
    public String getPrompt() { return prompt; }
    public String getDefaultValue() { return defaultValue; }
    public boolean isRequired() { return required; }
}