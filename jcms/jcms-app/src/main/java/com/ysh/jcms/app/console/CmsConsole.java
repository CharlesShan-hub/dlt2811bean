package com.ysh.jcms.app.console;

import com.ysh.jcms.app.console.api.CliApiServer;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.node.SclManager;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.CommandInfo.Requirement;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Contract for CMS interactive consoles.
 *
 * <p>All methods have default implementations that delegate to the underlying
 * {@link CmsNode} via {@code ((CmsNode) this)}.  Concrete classes only need to
 * override {@link #registerHandlers()}.
 */
public interface CmsConsole {

    /* ====== handler registration ====== */

    /** Register a command handler. */
    default void register(CommandHandler handler) {
        ((CmsNode) this).registerConsoleHandler(handler);
    }

    /** Return the registered handlers by command name. */
    default Map<String, CommandHandler> handlers() {
        return ((CmsNode) this).consoleHandlers();
    }

    /** Subclasses register their command handlers here. */
    default void registerHandlers() {
    }

    /* ====== state queries ====== */

    /** {@code true} when TCP connected AND session is ASSOCIATED. */
    default boolean connected() {
        CmsNode node = (CmsNode) this;
        return node.clientConnected()
            && node.client() != null
            && node.client().session() != null
            && node.client().session().state() == com.ysh.jcms.utils.transport.session.SessionState.ASSOCIATED;
    }

    /** {@code true} when TCP-level connection is established (association not required). */
    default boolean clientConnected() {
        return ((CmsNode) this).clientConnected();
    }

    /** Current associated access-point reference (IED/AP), or {@code null}. */
    default String associatedAp() {
        CmsNode node = (CmsNode) this;
        if (!connected() || node.client() == null || node.client().session() == null)
            return null;
        return node.client().session().associatedApRef();
    }

    /** {@code true} when the TCP connection uses TLS. */
    default boolean tlsConnected() {
        CmsNode node = (CmsNode) this;
        return node.client() != null && node.client().tls();
    }

    /** {@code true} when the association uses application-layer security. */
    default boolean associatedSecure() {
        CmsNode node = (CmsNode) this;
        return connected() && node.client() != null && node.client().session() != null
            && node.client().session().associatedSecure();
    }

    /* ====== client access ====== */

    @SuppressWarnings("unchecked")
    default <T> T getClient(Class<T> type) {
        return ((CmsNode) this).getClient(type);
    }

    /* ====== node operations (delegated to underlying CmsNode) ====== */

    default void connect(String host, int port) throws IOException {
        ((CmsNode) this).connect(host, port);
    }

    default void connectTls(String host, int port, SSLContext sslContext) throws IOException {
        ((CmsNode) this).connectTls(host, port, sslContext);
    }

    default void close() {
        ((CmsNode) this).close();
    }

    default SclManager sclManager() {
        return ((CmsNode) this).sclManager();
    }

    /* ====== lifecycle ====== */

    /** Start the interactive console loop. */
    default void run() {
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
            CmsPrinter.gray("Auto-exec: " + autoExec);
            for (String cmd : autoExec.split(";")) {
                String trimmed = cmd.trim();
                if (!trimmed.isEmpty())
                    executeLine(trimmed);
            }
        }

        boolean running = true;
        LineReader reader = LineReaderBuilder.builder()
            .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_console_history"))
            .build();

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

        // Close JLine reader/terminal to restore console mode (important on Windows)
        try {
            reader.getTerminal().close();
        } catch (Exception ignored) {
            // best-effort
        }
        System.exit(0);
    }

    /** Override to customise the prompt (default "cms> "). */
    default String prompt() {
        return "cms> ";
    }

    /** Hook called before the loop starts. */
    default void onStart() {
        CmsNode node = (CmsNode) this;
        if (node.server() != null) {
            // Server-side: start CMS server
            try {
                node.start(false);
                System.out.println("CMS Server running on port " + node.server().port());
                if (node.server().tls())
                    System.out.println("TLS port: " + node.server().sslPort());
                System.out.println("SCL loaded: " + node.sclManager().loaded());
                if (node.sclManager().loaded())
                    System.out.println("SCL file: " + node.sclManager().source());
                System.out.println("Type 'exit' to stop...");
            } catch (Exception e) {
                System.err.println("Failed to start server: " + e.getMessage());
            }
        } else {
            // Client-side: start embedded API server (for remote execution via cms.ps1)
            String apiEnabled = System.getProperty("cms.api.enabled", "true");
            if (!"false".equalsIgnoreCase(apiEnabled)) {
                int apiPort = Integer.parseInt(
                    System.getProperty("cms.api.port",
                        String.valueOf(CmsConfigLoader.load().client().console().apiPort())));
                try {
                    CliApiServer apiServer = new CliApiServer(apiPort, this);
                    apiServer.start();
                } catch (Exception e) {
                    CmsPrinter.gray("ApiServer not started (port " + apiPort + "): " + e.getMessage());
                }
            }
        }
    }

    /** Hook called after the loop ends. */
    default void onStop() {
        System.out.println("Bye.");
    }

    /* ====== precondition helpers ====== */

    /** Check associated, output error and return {@code false} if not. */
    default boolean requireAssociated(Map<String, String> args) {
        if (connected())
            return true;
        CmsPrinter.error("Not associated. Use 'associate' first.");
        return false;
    }

    /** Check TCP-level connected, output error and return {@code false} if not. */
    default boolean requireTcpConnected(Map<String, String> args) {
        if (((CmsNode) this).clientConnected())
            return true;
        CmsPrinter.error("Not connected. Use 'connect' first.");
        return false;
    }

    /** Check required param exists, output error and return {@code false} if missing. */
    static boolean requireParam(Map<String, String> args, String key, String usage) {
        String v = args.get(key);
        if (v != null && !v.isEmpty())
            return true;
        CmsPrinter.error("Missing --" + key + ".");
        return false;
    }

    /* ====== command parsing ====== */

    /**
     * Execute a single command line.
     * <p>
     * Tokenizes the input, resolves the command handler, parses {@code --key value}
     * arguments, checks preconditions, and delegates to the handler.
     */
    default boolean executeLine(String raw) {
        List<String> tokens = tokenize(raw);
        if (tokens.isEmpty())
            return true;

        String cmdName = tokens.get(0).toLowerCase();
        List<String> argTokens = tokens.subList(1, tokens.size());

        CommandHandler handler = handlers().get(cmdName);
        if (handler == null) {
            CmsPrinter.error("Unknown command: " + cmdName + "  (type 'help')");
            return false;
        }

        try {
            // Build args: default values from params(), overridden by --name value
            Map<String, String> args = new LinkedHashMap<>();
            for (Param p : (List<Param>) handler.params()) {
                if (p.defaultValue() != null) {
                    args.put(p.cliName(), p.defaultValue());
                }
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
                CmsPrinter.error("Not associated. Use 'associate' first.");
                return false;
            }
            if (req == Requirement.CONNECTED && !((CmsNode) this).clientConnected()) {
                CmsPrinter.error("Not connected. Use 'connect' first.");
                return false;
            }

            handler.execute(this, args);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null)
                msg = e.getClass().getSimpleName();
            CmsPrinter.error(msg);
            return false;
        }
        return true;
    }

    /** Tokenize a command line, respecting double-quote boundaries. */
    static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length() && s.charAt(i + 1) == '"') {
                buf.append('"');
                i++;
            } else if (c == '"') {
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
    static int findCommentStart(String s) {
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length() && s.charAt(i + 1) == '"') {
                i++;
                continue;
            }
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