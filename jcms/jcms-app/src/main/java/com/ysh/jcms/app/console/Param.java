package com.ysh.jcms.app.console;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * Describes a single CLI parameter of a command.
 *
 * <p>
 * Uses Lombok fluent accessors ({@code @Accessors(fluent = true)}) so a param
 * is declared in two lines, e.g.
 * {@code Param.of("after", "", "referenceAfter", String.class, false).desp("...")}.
 * A {@code null} {@link #daoName} means the parameter is display-only (shown in
 * help) and is not bound to the request DAO.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class Param {

    public static Param of(String cliName, String defaultValue, String daoName, Class<?> type, boolean required) {
        Param p = new Param();
        p.cliName = cliName;
        p.defaultValue = defaultValue;
        p.daoName = daoName;
        p.type = type;
        p.required = required;
        return p;
    }

    /** CLI flag name, e.g. "after" for {@code --after}. */
    private String cliName = "";

    /** Help text shown by the {@code help} command. */
    private String desp = "";

    /** Default value used when the flag is omitted; null = no default. */
    private String defaultValue;

    /**
     * Fluent field/method name on the request DAO to bind to; null = display-only.
     */
    private String daoName;

    /** Value type for conversion; defaults to {@code String.class}. */
    private Class<?> type = String.class;

    /**
     * Delimiter regex used by {@link #convert(String)} to split a string into a
     * list. Defaults to {@code "\\s+"} (whitespace). Only effective when
     * {@link #type} is {@code List.class}.
     */
    private String delimiter = "\\s+";

    /** Whether the flag is required. */
    private boolean required;

    /**
     * Converts a raw CLI string value to the target {@link #type}.
     * <ul>
     * <li>{@code String.class} — returned as-is</li>
     * <li>{@code Integer.class / int.class} — {@link Integer#parseInt}</li>
     * <li>{@code Long.class / long.class} — {@link Long#parseLong}</li>
     * <li>{@code Boolean.class / boolean.class} — {@link Boolean#parseBoolean}</li>
     * <li>{@code Byte.class / byte.class} — {@link Byte#parseByte}</li>
     * <li>{@code Short.class / short.class} — {@link Short#parseShort}</li>
     * <li>{@code Float.class / float.class} — {@link Float#parseFloat}</li>
     * <li>{@code Double.class / double.class} — {@link Double#parseDouble}</li>
     * <li>{@code List.class} — split by {@link #delimiter}</li>
     * </ul>
     */
    public Object convert(String value) {
        if (type == String.class)
            return value;
        if (type == Integer.class || type == int.class)
            return Integer.parseInt(value);
        if (type == Long.class || type == long.class)
            return Long.parseLong(value);
        if (type == Boolean.class || type == boolean.class)
            return Boolean.parseBoolean(value);
        if (type == Byte.class || type == byte.class)
            return Byte.parseByte(value);
        if (type == Short.class || type == short.class)
            return Short.parseShort(value);
        if (type == Float.class || type == float.class)
            return Float.parseFloat(value);
        if (type == Double.class || type == double.class)
            return Double.parseDouble(value);
        if (type == List.class)
            return Arrays.asList(value.split(delimiter));
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
