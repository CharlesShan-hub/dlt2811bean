package com.ysh.jcms.app.cli;

import java.util.List;
import java.util.Map;

public interface CommandHandler {

    String name();

    String description();

    List<Param> params();

    /**
     * Execute the command.
     *
     * @param ctx    the CLI context (holds node, content, etc.)
     * @param args   parsed argument values keyed by param name
     */
    void execute(CliContext ctx, Map<String, String> args) throws Exception;
}
