package com.ysh.dlt2811bean.cli;

import lombok.Getter;

import java.util.List;

@Getter
public class Param {

    public enum Type {
        PLAIN,
        ENUM,
        LD_NAME,
        LN_NAME,
        LN_REF,
        DA_REF,
        DA_TARGET,
        DS_REF,
        DA_NAME,
        DA_NAME_NOT_NULL,
        REFERENCE,
        BRCB_REF,
        URCB_REF
    }

    public enum ValueType {
        STRING,
        BOOLEAN,
        INTEGER
    }

    private final String name;
    private final String prompt;
    private final String defaultValue;
    private final boolean required;
    private final List<EnumChoice> enumChoices;
    private Type type = Type.PLAIN;
    private ValueType valueType = ValueType.STRING;
    private Object value;

    public void setValue(Object value) { this.value = value; }

    public Param valueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    @Getter
    public static class EnumChoice {
        public final String value;
        public final String label;

        public EnumChoice(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    public Param type(Type type) {
        this.type = type;
        return this;
    }

    public Param(String name, String prompt, String defaultValue) {
        this(name, prompt, defaultValue, false, List.of(), ValueType.STRING);
    }

    public Param(String name, String prompt, String defaultValue, boolean required) {
        this(name, prompt, defaultValue, required, List.of(), ValueType.STRING);
    }

    public Param(String name, String prompt, String defaultValue, List<EnumChoice> enumChoices) {
        this(name, prompt, defaultValue, false, enumChoices, ValueType.STRING);
    }

    public Param(String name, String prompt, String defaultValue, boolean required, List<EnumChoice> enumChoices) {
        this(name, prompt, defaultValue, required, enumChoices, ValueType.STRING);
    }

    public Param(String name, String prompt, ValueType valueType, String defaultValue) {
        this(name, prompt, defaultValue, false, List.of(), valueType);
    }

    public Param(String name, String prompt, String defaultValue, boolean required, List<EnumChoice> enumChoices, ValueType valueType) {
        this.name = name;
        this.prompt = prompt;
        this.defaultValue = defaultValue;
        this.required = required;
        this.enumChoices = enumChoices;
        this.valueType = valueType;
    }

    public static Param fc() {
        return fc("功能约束");
    }

    public static Param fc(String prompt) {
        return new Param("fc", prompt, "XX", com.ysh.dlt2811bean.service.info.FcInfo.enumChoices());
    }
}
