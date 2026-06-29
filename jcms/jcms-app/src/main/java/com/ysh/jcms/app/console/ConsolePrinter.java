package com.ysh.jcms.app.console;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

public final class ConsolePrinter {

    private ConsolePrinter() {}

    private static final String RST  = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRN  = "\u001B[32m";
    private static final String RED  = "\u001B[31m";
    private static final String GRY  = "\u001B[90m";

    /** Write to stdout FD as UTF-8 (console code page is 65001). */
    private static final FileOutputStream STDOUT;

    static {
        FileOutputStream fd = null;
        try {
            fd = new FileOutputStream(java.io.FileDescriptor.out);
        } catch (Exception ignored) {}
        STDOUT = fd;
    }

    private static void println(String s) {
        if (STDOUT != null) {
            try {
                STDOUT.write((s + "\n").getBytes(StandardCharsets.UTF_8));
                STDOUT.flush();
                return;
            } catch (IOException ignored) {}
        }
        System.out.println(s);
        System.out.flush();
    }

    public static void info(String msg)    { println(CYAN + "  " + msg + RST); }
    public static void success(String msg) { println(GRN  + "  OK  " + msg + RST); }
    public static void error(String msg)   { println(RED  + "  ERR " + msg + RST); }
    public static void gray(String msg)    { println(GRY  + "  " + msg + RST); }

    public static <T> void list(String title, List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) { gray(title + ": (empty)"); return; }
        info(title + ":");
        for (int i = 0; i < items.size(); i++)
            println(GRY + "    [" + i + "] " + RST + formatter.apply(items.get(i)));
    }
}
