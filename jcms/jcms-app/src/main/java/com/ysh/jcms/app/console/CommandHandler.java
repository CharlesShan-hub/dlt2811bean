package com.ysh.jcms.app.console;

import java.util.List;
import java.util.Map;

/**
 * Base class for CLI command handlers.
 *
 * <p>
 * Subclasses pass a {@link CommandInfo} constant to the constructor, which
 * provides the {@link #name()} and {@link #description()} automatically. Only
 * {@link #params()} and {@link #execute(CmsConsole, Map)} need to be
 * implemented.
 *
 * <p>
 * Usage:
 *
 * <pre>
 * {@code
 * public class AbortConsole extends CommandHandler {
 *     public AbortConsole() {
 *         super(CommandInfo.ABORT);
 *     }
 *     &#64;Override
 *     public List<Param> params() { ... }
 *     &#64;Override
 *     public void execute(CmsConsole console, Map<String, String> args) { ... }
 * }
 * }
 * </pre>
 */
public abstract class CommandHandler {

    private final CommandInfo info;

    protected CommandHandler(CommandInfo info) {
        this.info = info;
    }

    /** Command name used as the lookup key in {@link CmsConsole}. */
    public final String name() {
        return info.name();
    }

    /** Short description shown in help output. */
    public final String description() {
        return info.description();
    }

    /** Returns the {@link CommandInfo} constant associated with this handler. */
    public final CommandInfo info() {
        return info;
    }

    /** Parameter definitions for command-line parsing. */
    public abstract List<Param> params();

    /** Execute the command with parsed arguments. */
    public abstract void execute(CmsConsole console, Map<String, String> args) throws Exception;
}
