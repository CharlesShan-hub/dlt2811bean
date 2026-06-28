package com.ysh.jcms.app.console;

import java.util.List;
import java.util.function.Function;

public final class ConsolePrinter {

    private ConsolePrinter() {}

    private static final String RST  = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRN  = "\u001B[32m";
    private static final String RED  = "\u001B[31m";
    private static final String GRY  = "\u001B[90m";

    public static void info(String msg)    { System.out.println(CYAN + "  " + msg + RST); }
    public static void success(String msg) { System.out.println(GRN  + "  OK  " + msg + RST); }
    public static void error(String msg)   { System.out.println(RED  + "  ERR " + msg + RST); }
    public static void gray(String msg)    { System.out.println(GRY  + "  " + msg + RST); }

    public static <T> void list(String title, List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) { gray(title + ": (empty)"); return; }
        info(title + ":");
        for (int i = 0; i < items.size(); i++)
            System.out.println(GRY + "    [" + i + "] " + RST + formatter.apply(items.get(i)));
    }
}
