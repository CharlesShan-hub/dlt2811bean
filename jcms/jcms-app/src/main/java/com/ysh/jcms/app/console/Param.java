package com.ysh.jcms.app.console;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Describes a single CLI parameter of a command.
 *
 * <p>
 * Uses Lombok fluent accessors ({@code @Accessors(fluent = true)}) so a param
 * is declared in one chain, e.g. {@code new Param().cliName("after")
 * .description("...").daoName("referenceAfter")}. A {@code null}
 * {@link #daoName} means the parameter is display-only (shown in help) and is
 * not bound to the request DAO.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class Param {

    public enum ParamType {
        STRING(String.class), INT(int.class), INTEGER(Integer.class), LONG(long.class), BOOLEAN(boolean.class), BYTE(byte.class),
        SHORT(short.class), FLOAT(float.class), DOUBLE(double.class);

        private final Class<?> javaType;

        ParamType(Class<?> javaType) {
            this.javaType = javaType;
        }

        public Class<?> javaType() {
            return javaType;
        }
    }

    /** CLI flag name, e.g. "after" for {@code --after}. */
    private String cliName = "";

    /** Help text shown by the {@code help} command. */
    private String description = "";

    /** Default value used when the flag is omitted; null = no default. */
    private String defaultValue;

    /** Fluent field/method name on the request DAO to bind to; null = display-only. */
    private String daoName;

    /** Value type for conversion; defaults to STRING. */
    private ParamType type = ParamType.STRING;

    /** Whether the flag is required. */
    private boolean required;
}
