package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.*;

public class HelpHandler implements CommandHandler {

    private final CliContext ctx;

    public HelpHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    public String getName() { return "help"; }
    public String getDescription() { return "显示帮助信息"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        System.out.println("\n" + CmsColor.bold("可用命令:") + "\n");
        System.out.println(CmsColor.gray("  " + ctx.padRight("命令", 17) + String.format("%-7s", "章节") + String.format("%-5s", "服务码") + " 描述"));
        System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
        List<CommandHandler> sorted = new ArrayList<>(ctx.getHandlers().values());
        sorted.sort((a, b) -> {
            ServiceInfo ai = ServiceInfo.byCliName(a.getName());
            ServiceInfo bi = ServiceInfo.byCliName(b.getName());
            if (ai == null && bi == null) return a.getName().compareTo(b.getName());
            if (ai == null) return 1;
            if (bi == null) return -1;
            return compareSection(ai.getSection(), bi.getSection());
        });
        for (CommandHandler h : sorted) {
            if (h.getName().equals("help") || h.getName().equals("exit")) continue;
            ServiceInfo info = ServiceInfo.byCliName(h.getName());
            if (info != null) {
                System.out.println("  " + CmsColor.bold(ctx.padRight(h.getName(), 18))
                        + CmsColor.cyan(String.format("%-8s", "[" + info.getSection() + "]"))
                        + " " + CmsColor.yellow(String.format("0x%02X", info.getServiceCode()))
                        + " " + info.getDescription());
            } else {
                System.out.println("  " + CmsColor.bold(ctx.padRight(h.getName(), 18))
                         + String.format("%-8s", "")
                         + String.format("%-6s", "")
                        + " " + h.getDescription());
            }
        }
        System.out.println("  " + CmsColor.bold(ctx.padRight("help", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 显示帮助信息");
        System.out.println("  " + CmsColor.bold(ctx.padRight("exit", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 退出程序");
        System.out.println(CmsColor.gray("\nUse: help <command> 查看命令详细用法  |  help datatype 查看数据类型  |  help cdc 查看公用数据类  |  Tab 键可补全命令"));
    }

    private static int compareSection(String a, String b) {
        String[] pa = a.split("\\.");
        String[] pb = b.split("\\.");
        for (int i = 0; i < Math.min(pa.length, pb.length); i++) {
            int cmp = Integer.parseInt(pa[i]) - Integer.parseInt(pb[i]);
            if (cmp != 0) return cmp;
        }
        return pa.length - pb.length;
    }

    public void printCommandHelp(String cmdName, CommandHandler h) {
        ServiceInfo info = ServiceInfo.byCliName(cmdName);
        System.out.println("\n  " + CmsColor.bold(cmdName) + " - "
                + (info != null ? (CmsColor.cyan("[" + info.getSection() + "] ") + info.getDescription()) : h.getDescription()));
        if (info != null) {
            if (!info.getDescriptionDetail().isEmpty()) {
                System.out.println("\n  " + CmsColor.cyan("功能介绍: "));
                for (String line : info.getDescriptionDetail().split("\n")) {
                    System.out.println("    " + line);
                }
            }
            if (!info.getAsn1Definition().isEmpty()) {
                System.out.println("\n  " + CmsColor.cyan("ASN.1 定义: "));
                for (String line : info.getAsn1Definition().split("\n")) {
                    System.out.println("    " + line);
                }
            }
            System.out.println("\n  " + CmsColor.green("用法: ") + info.getUsage());
        }
        for (Param param : h.getParams()) {
            System.out.println("    " + CmsColor.cyan(param.getName()) + "  " + param.getPrompt()
                    + (param.getDefaultValue() != null && !param.getDefaultValue().isEmpty() ? CmsColor.gray(" (默认: " + param.getDefaultValue() + ")") : ""));
        }
        if (cmdName.equals("cli-setting")) {
            System.out.println("\n  " + CmsColor.bold("可用配置项:"));
            System.out.println("    " + CmsColor.cyan("trace-pdu") + CmsColor.gray("    true/false  ") + "显示请求/响应 PDU 报文");
        }
    }

    public void printDatatypeHelp(DataTypeInfo dt) {
        System.out.println("\n  " + CmsColor.bold(dt.getTypeName()) + " - "
                + CmsColor.cyan("[" + dt.getSection() + "] ") + dt.getDescription());
        if (!dt.getAsn1Definition().isEmpty()) {
            System.out.println("\n  " + CmsColor.cyan("ASN.1 定义: "));
            for (String line : dt.getAsn1Definition().split("\n")) {
                System.out.println("    " + line);
            }
        }
    }

    public void printDatatypeList() {
        System.out.println("\n" + CmsColor.bold("可用数据类型:") + "\n");
        System.out.println(CmsColor.gray("  " + ctx.padRight("类型名", 22) + String.format("%-7s", "章节") + " 描述"));
        System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
        List<DataTypeInfo> sorted = new ArrayList<>(Arrays.asList(DataTypeInfo.values()));
        sorted.sort((a, b) -> compareSection(a.getSection(), b.getSection()));
        for (DataTypeInfo dt : sorted) {
            System.out.println("  " + CmsColor.bold(ctx.padRight(dt.getTypeName(), 22))
                    + CmsColor.cyan(String.format("%-8s", "[" + dt.getSection() + "]"))
                    + " " + dt.getDescription());
        }
        System.out.println(CmsColor.gray("\nUse: help datatype <name> 查看数据类型详细定义"));
    }

    public void printCdcList() {
        System.out.println("\n" + CmsColor.bold("可用公用数据类 (CDC):") + "\n");
        System.out.println(CmsColor.gray("  " + ctx.padRight("CDC", 8) + " 中文名" + String.format("%-14s", "") + "英文描述"));
        System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
        for (CdcInfo cdc : CdcInfo.values()) {
            System.out.println("  " + CmsColor.bold(ctx.padRight(cdc.getName(), 8))
                    + CmsColor.cyan(ctx.padRight(cdc.getChineseName(), 16))
                    + " " + cdc.getDescription());
        }
        System.out.println(CmsColor.gray("\nUse: help cdc <name> 查看 CDC 详细定义"));
    }

    public void printCdcHelp(CdcInfo cdc) {
        System.out.println("\n  " + CmsColor.bold(cdc.getName()) + " - " + CmsColor.cyan(cdc.getChineseName()));
        System.out.println("\n  " + CmsColor.green("英文名: ") + cdc.getDescription());
    }
}
