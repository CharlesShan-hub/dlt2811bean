package com.ysh.jcms.app.console;

public class Param {

    private final String name;
    private final String description;
    private final String defaultValue;

    public Param(String name, String description) { this(name, description, null); }

    public Param(String name, String description, String defaultValue) {
        this.name = name; this.description = description; this.defaultValue = defaultValue;
    }

    public String name() { return name; }
    public String description() { return description; }
    public String defaultValue() { return defaultValue; }
}
