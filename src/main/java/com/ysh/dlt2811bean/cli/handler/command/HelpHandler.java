package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.FcInfo;
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
        List<CommandHandler> sorted = new ArrayList<>(ctx.getHandlers().values());
        sorted.sort((a, b) -> {
            ServiceInfo ai = ServiceInfo.byCliName(a.getName());
            ServiceInfo bi = ServiceInfo.byCliName(b.getName());
            if (ai == null && bi == null) return a.getName().compareTo(b.getName());
            if (ai == null) return 1;
            if (bi == null) return -1;
            return compareSection(ai.getSection(), bi.getSection());
        });
        String lastSection = "";
        for (CommandHandler h : sorted) {
            if (h.getName().equals("help") || h.getName().equals("exit")) continue;
            ServiceInfo info = ServiceInfo.byCliName(h.getName());
            if (info != null) {
                String major = info.getSection().replaceAll("(\\d+\\.\\d+).*", "$1");
                if (!major.equals(lastSection)) {
                    lastSection = major;
                    System.out.println();
                    System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
                    System.out.println("  " + CmsColor.cyan(major + " " + sectionTitle(major)));
                }
                System.out.println("  " + CmsColor.bold(ctx.padRight(h.getName(), 18))
                        + CmsColor.cyan(String.format("%-8s", "[" + info.getSection() + "]"))
                        + " " + CmsColor.yellow(String.format("0x%02X", info.getServiceCode()))
                        + " " + info.getDescription());
            }
        }
        System.out.println();
        System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
        System.out.println("  " + CmsColor.cyan("控制台命令"));
        for (CommandHandler h : sorted) {
            if (h.getName().equals("help") || h.getName().equals("exit")) continue;
            ServiceInfo info = ServiceInfo.byCliName(h.getName());
            if (info == null) {
                System.out.println("  " + CmsColor.bold(ctx.padRight(h.getName(), 18))
                         + String.format("%-8s", "")
                         + String.format("%-6s", "")
                        + " " + h.getDescription());
            }
        }
        System.out.println("  " + CmsColor.bold(ctx.padRight("help", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 显示帮助信息");
        System.out.println("  " + CmsColor.bold(ctx.padRight("exit", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 退出程序");
        System.out.println(CmsColor.gray("\nUse: help <command> 查看命令详细用法  |  help datatype 查看数据类型  |  help cdc 查看公用数据类  |  help fc 查看功能约束  |  Tab 键可补全命令"));
    }

    private static String sectionTitle(String section) {
        return switch (section) {
            case "8.2" -> "关联服务";
            case "8.3" -> "服务器、逻辑设备、逻辑节点目录服务";
            case "8.4" -> "数据类服务";
            case "8.5" -> "数据集服务";
            case "8.6" -> "定值组服务";
            case "8.7" -> "报告服务";
            case "8.8" -> "日志服务";
            case "8.9" -> "通用变电站事件类服务";
            case "8.10" -> "多播采样值类服务";
            case "8.11" -> "控制服务";
            case "8.12" -> "文件服务";
            case "8.13" -> "远程过程调用";
            case "8.14" -> "测试服务";
            case "8.15" -> "协商服务";
            default -> "";
        };
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
        for (Param param : h.updateConfigAndGetParams()) {
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

    public void printFcList() {
        System.out.println("\n" + CmsColor.bold("功能约束 (FC):") + "\n");
        System.out.println(CmsColor.gray("  FC   中文名          说明"));
        for (FcInfo fc : FcInfo.values()) {
            System.out.println("  " + CmsColor.bold(ctx.padRight(fc.getName(), 4))
                    + " " + CmsColor.cyan(ctx.padRight(fc.getChineseName(), 12))
                    + " " + fc.getDescription());
        }
    }

    public void printFcHelp(FcInfo fc) {
        System.out.println("\n  " + CmsColor.bold(fc.getName()) + " - " + CmsColor.cyan(fc.getChineseName()));
        System.out.println("\n  " + CmsColor.green("说明: ") + fc.getDescription());
    }

    public void printSectionCommands(String section) {
        System.out.println("\n" + CmsColor.bold("章节 " + section + " " + sectionTitle(section) + " 下的命令:") + "\n");
        System.out.println(CmsColor.gray("  " + ctx.padRight("命令", 17) + String.format("%-7s", "章节") + String.format("%-5s", "服务码") + " 描述"));
        String prefix = section + ".";
        List<CommandHandler> matched = new ArrayList<>();
        for (CommandHandler h : ctx.getHandlers().values()) {
            if (h.getName().equals("help") || h.getName().equals("exit")) continue;
            ServiceInfo info = ServiceInfo.byCliName(h.getName());
            if (info != null && info.getSection().startsWith(prefix)) {
                matched.add(h);
            }
        }
        matched.sort((a, b) -> compareSection(
                ServiceInfo.byCliName(a.getName()).getSection(),
                ServiceInfo.byCliName(b.getName()).getSection()));
        if (matched.isEmpty()) {
            System.out.println(CmsColor.gray("  无命令"));
        } else {
            for (CommandHandler h : matched) {
                ServiceInfo info = ServiceInfo.byCliName(h.getName());
                System.out.println("  " + CmsColor.bold(ctx.padRight(h.getName(), 18))
                        + CmsColor.cyan(String.format("%-8s", "[" + info.getSection() + "]"))
                        + " " + CmsColor.yellow(String.format("0x%02X", info.getServiceCode()))
                        + " " + info.getDescription());
            }
        }
    }
}
