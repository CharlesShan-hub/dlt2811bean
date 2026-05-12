package com.ysh.dlt2811bean.cli;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Param {
    private final String name;
    private final String prompt;
    private final String defaultValue;
    private final boolean required;
    private final List<EnumChoice> enumChoices;

    @Getter
    @AllArgsConstructor
    public static class EnumChoice {
        public final String value;
        public final String label;
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

    public static Param fc() {
        return fc("功能约束");
    }

    public static Param fc(String prompt) {
        return new Param("fc", prompt, "XX", com.ysh.dlt2811bean.service.info.FcInfo.enumChoices());
    }
}