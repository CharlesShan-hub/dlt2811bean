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

    /** 输出 JSON 成功响应：{"success":true,"info":"...","data":null}。 */
    public static void success(String msg) {
        raw("{\"success\":true,\"info\":\"" + CmsFormatUtil.escapeJson(msg) + "\",\"data\":null}");
    }

    /** 输出 JSON 错误响应：{"success":false,"info":"...","data":null}。 */
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
     * 直接序列化原始对象为 JSON 输出，不做任何包装。
     * <p>
     * 调用方应直接传入要输出的数据对象（如 List、Map、POJO）， 该方法会将其序列化为 JSON 并输出。
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
     * JSON 语法高亮：键=青色，字符串值=绿色，布尔/数字=黄色，null=灰色。 仅用于终端输出（CLI 模式），API server 模式输出原始
     * JSON。
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
