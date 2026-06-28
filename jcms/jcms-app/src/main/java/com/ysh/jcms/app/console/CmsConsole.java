package com.ysh.jcms.app.console;

import com.ysh.jcms.app.node.CmsNode;
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
 * <p>Extends {@link CmsNode} and provides JLine-based interactive command processing.
 * Subclasses register their own handlers (client commands or server handlers)
 * via {@link #register(CommandHandler)} / {@link #registerHandlers()}.
 */
public abstract class CmsConsole extends CmsNode {

    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final LineReader reader;
    private boolean running = true;

    protected CmsConsole(boolean createServer) {
        super(createServer);
        this.reader = LineReaderBuilder.builder()
            .variable(LineReader.HISTORY_FILE,
                Paths.get(System.getProperty("user.home"), ".cms_console_history"))
            .build();
    }

    /** Subclasses register their command handlers here. */
    protected abstract void registerHandlers();

    // ── handler management ──

    public void register(CommandHandler handler) {
        handlers.put(handler.name(), handler);
    }

    public Map<String, CommandHandler> handlers() { return handlers; }

    // ── console helpers (used by handlers) ──

    public boolean isConnected() {
        return isClientConnected()
            && getClient().getSession() != null
            && getClient().getSession().getState() == SessionState.ASSOCIATED;
    }

    // ── main loop ──

    public void run() {
        registerHandlers();
        onStart();
        while (running) {
            String raw;
            try {
                raw = reader.readLine(prompt()).trim();
            } catch (UserInterruptException | EndOfFileException e) {
                continue;
            } catch (Exception e) {
                continue;
            }
            if (raw.isEmpty()) continue;
            if (raw.equals("exit") || raw.equals("quit")) {
                running = false;
                break;
            }
            executeLine(raw);
        }
        if (isConnected()) close();
        onStop();
    }

    /** Override to customise the prompt (default "cms> "). */
    protected String prompt() { return "cms> "; }

    /** Hook called before the loop starts. */
    protected void onStart() {}

    /** Hook called after the loop ends. */
    protected void onStop() { System.out.println("Bye."); }

    // ── command parsing ──

    public boolean executeLine(String raw) {
        List<String> tokens = tokenize(raw);
        if (tokens.isEmpty()) return true;

        String cmdName = tokens.get(0).toLowerCase();
        List<String> argTokens = tokens.subList(1, tokens.size());

        CommandHandler handler = handlers.get(cmdName);
        if (handler == null) {
            ConsolePrinter.error("Unknown command: " + cmdName + "  (type 'help')");
            return true;
        }

        try {
            Map<String, String> args = new LinkedHashMap<>();
            List<Param> params = handler.params();
            Map<String, String> namedArgs = new LinkedHashMap<>();
            List<String> positionalArgs = new ArrayList<>();
            for (int i = 0; i < argTokens.size(); i++) {
                String t = argTokens.get(i);
                if (t.startsWith("--") && t.length() > 2) {
                    String key = t.substring(2);
                    String val = (i + 1 < argTokens.size() && !argTokens.get(i + 1).startsWith("--"))
                        ? argTokens.get(++i) : "";
                    namedArgs.put(key, val);
                } else {
                    positionalArgs.add(t);
                }
            }

            for (int i = 0; i < params.size(); i++) {
                Param p = params.get(i);
                if (namedArgs.containsKey(p.name())) {
                    args.put(p.name(), namedArgs.get(p.name()));
                } else if (i < positionalArgs.size()) {
                    args.put(p.name(), positionalArgs.get(i));
                } else if (p.defaultValue() != null) {
                    args.put(p.name(), p.defaultValue());
                } else {
                    args.put(p.name(), "");
                }
            }

            handler.execute(this, args);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) msg = e.getClass().getSimpleName();
            ConsolePrinter.error(msg);
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
                if (buf.length() > 0) { tokens.add(buf.toString()); buf.setLength(0); }
            } else {
                buf.append(c);
            }
        }
        if (buf.length() > 0) tokens.add(buf.toString());
        return tokens;
    }
}
