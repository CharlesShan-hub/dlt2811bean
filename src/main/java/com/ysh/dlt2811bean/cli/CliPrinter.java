package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.LnInfo;
import com.ysh.dlt2811bean.utils.CmsColor;

import java.util.List;
import java.util.function.Function;

public final class CliPrinter {

    private CliPrinter() {
    }

    public static void printRequestPdu(CliContext ctx, Object pdu) {
        ctx.printGrayPdu("  >> Request PDU:", pdu);
    }

    public static void printResponsePdu(CliContext ctx, Object pdu) {
        ctx.printGrayPdu("  << Response PDU:", pdu);
    }

    public static void printGray(String text) {
        System.out.println(CmsColor.gray(text));
    }

    public static void printRed(String text) {
        System.out.println(CmsColor.red(text));
    }

    public static void printGreen(String text) {
        System.out.println(CmsColor.green(text));
    }

    public static void printCyan(String text) {
        System.out.println(CmsColor.cyan(text));
    }

    public static String cyan(String text) {
        return CmsColor.cyan(text);
    }

    public static void printMoreFollows(boolean moreFollows) {
        if (moreFollows) {
            printGray("  (more data available)");
        }
    }

    public static boolean printIfEmpty(boolean isEmpty) {
        if (isEmpty) {
            printGray("  无数据");
            return true;
        }
        return false;
    }

    public static <T> void printList(String title, List<T> items, Function<T, String> formatter) {
        if (printIfEmpty(items.isEmpty())) return;
        printGreen("  " + title + ":");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("    " + cyan("[" + i + "]") + " " + formatter.apply(items.get(i)));
        }
    }

    public static String lnClassName(String lnRef) {
        if (lnRef == null) return "";
        int lastSlash = lnRef.lastIndexOf("/");
        if (lastSlash < 0) return "";
        if (lastSlash + 5 > lnRef.length()) return "";
        String className = lnRef.substring(lastSlash + 1, lastSlash + 5);
        LnInfo info = LnInfo.byName(className);
        return info != null ? " - " + info.getChineseName() : "";
    }
}
