package com.ysh.jcms.app.handler.console.server;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.BaseServerHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handler for the {@code max-entries} command.
 *
 * <p>
 * Sets the maximum number of entries returned per page by all paginated server
 * handlers. Useful for testing the auto-pull pagination logic on the client
 * side without needing a large SCL model.
 *
 * <p>
 * Usage:
 * <ul>
 * <li>{@code max-entries} — show current value</li>
 * <li>{@code max-entries 5} — set max entries per page to 5</li>
 * <li>{@code max-entries 0} — restore default (from config)</li>
 * </ul>
 */
public class MaxEntriesHandler extends CommandHandler {

    public MaxEntriesHandler() {
        super(CommandInfo.MAX_ENTRIES);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("value", "最大返回条数（0 恢复默认）", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String value = args.get("value");

        if (value == null || value.isEmpty()) {
            // Show current value
            int current = BaseServerHandler.getMaxPageSize();
            int configDefault = com.ysh.jcms.utils.config.CmsConfigLoader.load().protocol().maxArraySize();
            if (current <= 0) {
                ConsolePrinter.info("max-entries = " + configDefault + " (default, from config)");
            } else {
                ConsolePrinter.info("max-entries = " + current + " (override, config default is " + configDefault + ")");
            }
            return;
        }

        int v = Integer.parseInt(value);
        BaseServerHandler.setMaxPageSize(v);
        if (v <= 0) {
            ConsolePrinter.info("max-entries restored to default (config)");
        } else {
            ConsolePrinter.info("max-entries = " + v + " (set)");
        }
    }
}
