package com.ysh.jcms.core.util;

import com.ysh.jcms.data.InnerBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

public final class CmsPrinter {

    private CmsPrinter() {
    }

    private static final String RST = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRN = "\u001B[32m";
    private static final String YEL = "\u001B[33m";
    private static final String GRY = "\u001B[90m";
    /** Clear screen (ANSI ED2). */
    private static final String CLEAR_SCREEN = "\u001B[2J";
    /** Move cursor to top-left corner (ANSI CUP 1;1). */
    private static final String CURSOR_HOME = "\u001B[H";

    /** Write to stdout FD as UTF-8 (console code page is 65001). */
    private static final FileOutputStream STDOUT;

    /**
     * Per-thread capture output stream for API server (avoids concurrent request
     * corruption).
     */
    private static final ThreadLocal<OutputStream> captureStream = new ThreadLocal<>();

    static {
        FileOutputStream fd = null;
        try {
            fd = new FileOutputStream(java.io.FileDescriptor.out);
        } catch (Exception ignored) {
        }
        STDOUT = fd;
    }

    /**
     * Set a capture stream for the current thread (API server output). null =
     * restore to console.
     */
    public static void setCaptureStream(OutputStream os) {
        if (os == null) {
            captureStream.remove();
        } else {
            captureStream.set(os);
        }
    }

    private static void println(String s) {
        OutputStream cs = captureStream.get();
        if (cs != null) {
            try {
                cs.write((s + "\n").getBytes(StandardCharsets.UTF_8));
                cs.flush();
                return;
            } catch (IOException ignored) {
            }
        }
        if (STDOUT != null) {
            try {
                STDOUT.write((s + "\n").getBytes(StandardCharsets.UTF_8));
                STDOUT.flush();
                return;
            } catch (IOException ignored) {
            }
        }
        System.out.println(s);
        System.out.flush();
    }

    public static void info(String msg) {
        println(CYAN + "  " + msg + RST);
    }

    /** Clear the screen and move the cursor to the top-left corner. Skipped in API capture mode to avoid leaking escape sequences into responses. */
    public static void clear() {
        if (captureStream.get() != null) {
            return;
        }
        consoleOnly(CLEAR_SCREEN + CURSOR_HOME);
    }

    /** Output a JSON success response: {"success":true,"info":"...","data":null}. */
    public static void success(String msg) {
        raw("{\"success\":true,\"info\":\"" + CmsFormatUtil.escapeJson(msg) + "\",\"data\":null}");
    }

    /** Output a JSON error response: {"success":false,"info":"...","data":null}. */
    public static void error(String msg) {
        raw("{\"success\":false,\"info\":\"" + CmsFormatUtil.escapeJson(msg) + "\",\"data\":null}");
    }

    public static void gray(String msg) {
        println(GRY + "  " + msg + RST);
    }

    /**
     * Output raw text (no ANSI codes, no prefix). Passes through capture stream in
     * API mode.
     */
    public static void raw(String msg) {
        println(msg);
    }

    /**
     * Output text to the real console only, bypassing the API capture stream.
     * <p>
     * Used for debug/trace output (e.g. PDU trace, session trace) that should
     * appear in the interactive CLI but NOT be included in HTTP API responses.
     */
    public static void consoleOnly(String msg) {
        if (STDOUT != null) {
            try {
                STDOUT.write((msg + "\n").getBytes(StandardCharsets.UTF_8));
                STDOUT.flush();
            } catch (IOException ignored) {
            }
        } else {
            System.out.println(msg);
            System.out.flush();
        }
    }

    public static <T> void list(String title, List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) {
            gray(title + ": (empty)");
            return;
        }
        info(title + ":");
        for (int i = 0; i < items.size(); i++)
            println(GRY + "    [" + i + "] " + RST + formatter.apply(items.get(i)));
    }

    /**
     * Serialize an arbitrary object to JSON and print it without any wrapping.
     * <p>
     * Callers pass the object to print directly (e.g. a List, Map or POJO); it is
     * serialized to JSON and printed.
     */
    public static void outputJson(Object result) {
        try {
            String json = InnerBase.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            if (captureStream.get() == null) {
                json = highlightJson(json);
            }
            raw(json);
        } catch (Exception e) {
            error("serialization failed: " + e.getMessage());
        }
    }

    /**
     * Unified response wrapper: {"success":true,"info":"OK","data":<raw fields>}.
     * <p>
     * Wraps the raw data object into the unified success/info/data response format
     * and prints it.
     */
    public static void result(Object data) {
        try {
            java.util.LinkedHashMap<String, Object> wrapper = new java.util.LinkedHashMap<>();
            wrapper.put("success", true);
            wrapper.put("info", "OK");
            wrapper.put("data", data);
            String json = InnerBase.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
            if (captureStream.get() == null) {
                json = highlightJson(json);
            }
            raw(json);
        } catch (Exception e) {
            error("serialization failed: " + e.getMessage());
        }
    }

    /**
     * JSON syntax highlighting: keys = cyan, string values = green, booleans/numbers
     * = yellow, null = gray. Only used for terminal output (CLI mode); API server
     * mode prints raw JSON.
     */
    private static String highlightJson(String json) {
        json = json.replaceAll("(\"[^\"]*\")(\\s*:)", CYAN + "$1" + RST + "$2");
        json = json.replaceAll("(:\\s*)(\"[^\"]*\")", "$1" + GRN + "$2" + RST);
        json = json.replaceAll("(:\\s*)(true|false)", "$1" + YEL + "$2" + RST);
        json = json.replaceAll("(:\\s*)(null)", "$1" + GRY + "$2" + RST);
        json = json.replaceAll("(:\\s*)(-?\\d+\\.?\\d*)", "$1" + YEL + "$2" + RST);
        return json;
    }
}
