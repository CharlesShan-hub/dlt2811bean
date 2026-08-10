package com.ysh.jcms.app.console;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.util.CmsFormatUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class ConsolePrinter {

    private ConsolePrinter() {
    }

    private static final String RST = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YEL = "\u001B[33m";
    private static final String GRY = "\u001B[90m";
    private static final String BOLD = "\u001B[1m";

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

    public static <T> void list(String title, List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) {
            gray(title + ": (empty)");
            return;
        }
        info(title + ":");
        for (int i = 0; i < items.size(); i++)
            println(GRY + "    [" + i + "] " + RST + formatter.apply(items.get(i)));
    }

    /** 输出纯编号列表（不带标题行）。 */
    public static <T> void listItems(List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) {
            gray("（empty）");
            return;
        }
        for (int i = 0; i < items.size(); i++)
            println(GRY + "    [" + i + "] " + RST + formatter.apply(items.get(i)));
    }

    /**
     * 输出 JSON 成功响应，接受 {@code dao.result()} 返回的任意对象（内部自动识别 Map 类型）。
     */
    public static void outputJson(Object result) {
        if (result instanceof Map) {
            outputJson((Map<String, Object>) result);
        } else {
            error("result is not a Map");
        }
    }

    public static void outputJson(Map<String, Object> fields) {
        outputJson(fields, null);
    }

    public static void outputJson(Map<String, Object> fields, String info) {
        try {
            LinkedHashMap<String, Object> all = new LinkedHashMap<>();
            all.put("success", true);
            all.put("info", info);
            all.put("data", fields);
            String json = InnerBase.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(all);
            if (captureStream.get() == null) {
                json = highlightJson(json);
            }
            raw(json);
        } catch (Exception e) {
            error("serialization failed");
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
