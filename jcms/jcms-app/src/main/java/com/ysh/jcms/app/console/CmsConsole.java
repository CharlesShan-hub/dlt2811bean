package com.ysh.jcms.app.console;

import com.ysh.jcms.app.console.CommandInfo.Requirement;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.util.CmsFormatUtil;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;

import java.nio.file.Paths;
import java.util.*;

/**
 * Base class for CMS consoles.
 *
 * <p>
 * Extends {@link CmsNode} and provides JLine-based interactive command
 * processing. Subclasses register their own handlers (client commands or server
 * handlers) via {@link #register(CommandHandler)} /
 * {@link #registerHandlers()}.
 */
public abstract class CmsConsole extends CmsNode {

    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final LineReader reader;
    private boolean running = true;

    protected CmsConsole(boolean createServer) {
        super(createServer);
        this.reader = LineReaderBuilder.builder()
                .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_console_history")).build();
    }

    /** Subclasses register their command handlers here. */
    protected abstract void registerHandlers();

    // ── handler management ──

    public void register(CommandHandler handler) {
        handlers.put(handler.name(), handler);
    }

    public Map<String, CommandHandler> handlers() {
        return handlers;
    }

    // ── console helpers (used by handlers) ──

    public boolean connected() {
        return clientConnected() && client().session() != null && client().session().state() == SessionState.ASSOCIATED;
    }

    /** 当前关联的访问点引用（IED/AP），未关联时返回 null。 */
    public String associatedAp() {
        return connected() && client().session() != null ? client().session().associatedApRef() : null;
    }

    /** 当前 TCP 连接是否 TLS 加密。 */
    public boolean tlsConnected() {
        return client() != null && client().tls();
    }

    /** 当前关联是否使用应用层安全认证。 */
    public boolean associatedSecure() {
        return connected() && client().session() != null && client().session().associatedSecure();
    }

    // ── helpers for console command handlers ──

    /** Check associated, output error and return false if not. */
    public boolean requireAssociated(Map<String, String> args) {
        if (connected())
            return true;
        ConsolePrinter.error("Not associated. Use 'associate' first.");
        return false;
    }

    /**
     * Check TCP-level connected (association not required), output error and return
     * false if not.
     */
    public boolean requireTcpConnected(Map<String, String> args) {
        if (clientConnected())
            return true;
        ConsolePrinter.error("Not connected. Use 'connect' first.");
        return false;
    }

    /** Check required param exists, output error and return false if missing. */
    public static boolean requireParam(Map<String, String> args, String key, String usage) {
        String v = args.get(key);
        if (v != null && !v.isEmpty())
            return true;
        ConsolePrinter.error("Missing --" + key + ".");
        return false;
    }

    /**
     * @deprecated Use {@link ConsolePrinter#outputJson(java.util.Map)} directly.
     */
    @Deprecated
    public static <T> void outputList(String title, java.util.List<T> items, java.util.function.Function<T, String> formatter,
            Map<String, String> args, boolean numbered) {
        if (items == null || items.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            return;
        }
        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append("\"").append(CmsFormatUtil.escapeJson(formatter.apply(items.get(i)))).append("\"");
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }

    /**
     * @deprecated Use {@link ConsolePrinter#outputJson(java.util.Map)} directly.
     */
    @Deprecated
    public static <T> void outputList(String title, java.util.List<T> items, java.util.function.Function<T, String> formatter,
            Map<String, String> args) {
        outputList(title, items, formatter, args, false);
    }

    // ── main loop ──

    public void run() {
        registerHandlers();
        onStart();
        // Auto-exec: config autoExec first, then CMS_AUTO_EXEC env var
        String autoExec = CmsConfigLoader.load().client().console().autoExec();
        String envAutoExec = System.getenv("CMS_AUTO_EXEC");
        if (envAutoExec != null && !envAutoExec.isEmpty()) {
            if (autoExec == null || autoExec.isEmpty()) {
                autoExec = envAutoExec;
            } else {
                autoExec = autoExec + ";" + envAutoExec;
            }
        }
        if (autoExec != null && !autoExec.isEmpty()) {
            ConsolePrinter.gray("Auto-exec: " + autoExec);
            for (String cmd : autoExec.split(";")) {
                String trimmed = cmd.trim();
                if (!trimmed.isEmpty())
                    executeLine(trimmed);
            }
        }
        while (running) {
            String raw;
            try {
                raw = reader.readLine(prompt()).trim();
            } catch (UserInterruptException | EndOfFileException e) {
                continue;
            } catch (Exception e) {
                continue;
            }
            if (raw.isEmpty())
                continue;
            if (raw.equals("exit") || raw.equals("quit")) {
                running = false;
                break;
            }

            // Batch commands: split by ';', each line supports inline #/ // comments
            for (String cliLine : raw.split(";")) {
                String line = cliLine.trim();
                if (line.isEmpty())
                    continue;
                if (line.startsWith("#") || line.startsWith("//"))
                    continue;

                int commentIdx = findCommentStart(line);
                if (commentIdx >= 0) {
                    line = line.substring(0, commentIdx).trim();
                    if (line.isEmpty())
                        continue;
                }

                if (!executeLine(line)) {
                    break; // stop batch on first failure
                }
            }
        }
        if (connected())
            close();
        onStop();
        closeReader();
        System.exit(0);
    }

    /**
     * Close JLine reader/terminal to restore console mode (important on Windows).
     */
    private void closeReader() {
        try {
            reader.getTerminal().close();
        } catch (Exception ignored) {
            // best-effort
        }
    }

    /** Override to customise the prompt (default "cms> "). */
    protected String prompt() {
        return "cms> ";
    }

    /** Hook called before the loop starts. */
    protected void onStart() {
    }

    /** Hook called after the loop ends. */
    protected void onStop() {
        System.out.println("Bye.");
    }

    // ── command parsing ──

    public boolean executeLine(String raw) {
        List<String> tokens = tokenize(raw);
        if (tokens.isEmpty())
            return true;

        String cmdName = tokens.get(0).toLowerCase();
        List<String> argTokens = tokens.subList(1, tokens.size());

        CommandHandler handler = handlers.get(cmdName);
        if (handler == null) {
            ConsolePrinter.error("Unknown command: " + cmdName + "  (type 'help')");
            return false;
        }

        try {
            // Build args: default values from params(), overridden by --name value
            Map<String, String> args = new LinkedHashMap<>();
            for (Param p : handler.params()) {
                args.put(p.cliName(), p.defaultValue() != null ? p.defaultValue() : "");
            }
            for (int i = 0; i < argTokens.size(); i++) {
                String t = argTokens.get(i);
                if (t.startsWith("--") && t.length() > 2) {
                    String key = t.substring(2);
                    if (i + 1 < argTokens.size() && !argTokens.get(i + 1).startsWith("--")) {
                        args.put(key, argTokens.get(++i));
                    } else {
                        args.put(key, "true"); // boolean flag: --secure → true
                    }
                }
            }

            Requirement req = handler.requirement();
            if (req == Requirement.ASSOCIATED && !connected()) {
                ConsolePrinter.error("Not associated. Use 'associate' first.");
                return false;
            }
            if (req == Requirement.CONNECTED && !clientConnected()) {
                ConsolePrinter.error("Not connected. Use 'connect' first.");
                return false;
            }

            handler.execute(this, args);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null)
                msg = e.getClass().getSimpleName();
            ConsolePrinter.error(msg);
            return false;
        }
        return true;
    }

    private static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
            } else if (Character.isWhitespace(c) && !inQuote) {
                if (buf.length() > 0) {
                    tokens.add(buf.toString());
                    buf.setLength(0);
                }
            } else {
                buf.append(c);
            }
        }
        if (buf.length() > 0)
            tokens.add(buf.toString());
        return tokens;
    }

    /**
     * Find the start of an inline comment ({@code #} or {@code //}), respecting
     * double-quote boundaries.
     */
    private static int findCommentStart(String s) {
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
                continue;
            }
            if (inQuote)
                continue;
            if (c == '#' && (i == 0 || s.charAt(i - 1) != '\\'))
                return i;
            if (c == '/' && i + 1 < s.length() && s.charAt(i + 1) == '/')
                return i;
        }
        return -1;
    }
}
