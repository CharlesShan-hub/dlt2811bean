package com.ysh.jcms.app.cli;

import java.util.List;
import java.util.function.Function;

public final class CliPrinter {

    private CliPrinter() {}

    public static void info(String msg) {
        System.out.println("  " + msg);
    }

    public static void success(String msg) {
        System.out.println("  OK  " + msg);
    }

    public static void error(String msg) {
        System.out.println("  ERR " + msg);
    }

    public static void gray(String msg) {
        System.out.println("\u001B[90m  " + msg + "\u001B[0m");
    }

    public static <T> void list(String title, List<T> items, Function<T, String> formatter) {
        if (items.isEmpty()) {
            gray(title + ": (empty)");
            return;
        }
        info(title + ":");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("    [" + i + "] " + formatter.apply(items.get(i)));
        }
    }
}
