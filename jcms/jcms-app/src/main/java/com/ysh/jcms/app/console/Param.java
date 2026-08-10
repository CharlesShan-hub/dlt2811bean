package com.ysh.jcms.app.console;

public class Param {

    public enum ParamType {
        STRING(String.class), INT(int.class), INTEGER(Integer.class), LONG(long.class), BOOLEAN(boolean.class), BYTE(byte.class), SHORT(
                short.class), FLOAT(float.class), DOUBLE(double.class);

        private final Class<?> javaType;

        ParamType(Class<?> javaType) {
            this.javaType = javaType;
        }

        public Class<?> javaType() {
            return javaType;
        }
    }

    private final String name;
    private final String description;
    private final String defaultValue;
    private final String setter;
    private final ParamType type;
    private final boolean required;

    public Param(String name, String description) {
        this(name, description, null, null, ParamType.STRING, false);
    }

    public Param(String name, String description, String defaultValue) {
        this(name, description, defaultValue, null, ParamType.STRING, false);
    }

    public Param(String name, String description, String defaultValue, boolean required) {
        this(name, description, defaultValue, null, ParamType.STRING, required);
    }

    public Param(String name, String description, String defaultValue, String setter) {
        this(name, description, defaultValue, setter, ParamType.STRING, false);
    }

    public Param(String name, String description, String defaultValue, ParamType type) {
        this(name, description, defaultValue, name, type, false);
    }

    public Param(String name, String description, String defaultValue, ParamType type, boolean required) {
        this(name, description, defaultValue, name, type, required);
    }

    public Param(String name, String description, String defaultValue, String setter, ParamType type) {
        this(name, description, defaultValue, setter, type, false);
    }

    public Param(String name, String description, String defaultValue, String setter, ParamType type, boolean required) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.setter = setter != null ? setter : name;
        this.type = type;
        this.required = required;
    }

    public String name() { return name; }
    public String description() { return description; }
    public String defaultValue() { return defaultValue; }
    public String setter() { return setter; }
    public ParamType type() { return type; }
    public boolean required() { return required; }
}