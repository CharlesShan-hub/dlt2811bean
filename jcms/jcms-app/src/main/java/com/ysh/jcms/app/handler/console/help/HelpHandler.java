package com.ysh.jcms.app.handler.console.help;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.info.CmsCdcInfo;
import com.ysh.jcms.core.info.CmsDataTypeInfo;
import com.ysh.jcms.core.info.CmsFCInfo;
import com.ysh.jcms.core.info.CmsLnInfo;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.core.util.CmsPrinter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code help} command: command overview plus reference tables drawn from the
 * {@code jcms-core info} package (services, data types, FC, CDC, LN).
 *
 * <p>
 * Every lookup accepts either a name or its standard section number.
 *
 * <p>
 * Examples:
 * <pre>
 *   help                        # grouped overview of all commands
 *   help --cmd connect          # detail of a single command (params, requirement)
 *   help --service associate    # service info by CLI command name
 *   help --service 8.2.1        # service info by standard section number
 *   help --type CmsBoolean      # data type info by Java class name
 *   help --type 7.1.1           # data type info by standard section number
 *   help --fc ST                # one functional constraint
 *   help --cdc SPS              # one common data class
 *   help --ln PTOC              # one logical node
 *   help --fc                   # whole FC table (no value = full table)
 * </pre>
 */
public class HelpHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    private final CmsConsole console;

    public HelpHandler(CmsConsole console) {
        super(CommandInfo.HELP);
        this.console = console;
        param(Param.of("cmd", null, null, String.class, false), "单命令详情，如 --cmd connect");
        param(Param.of("service", null, null, String.class, false), "服务：命令名或章节号，如 --service associate / --service 8.2.1");
        param(Param.of("type", null, null, String.class, false), "数据类型：名/类名/章节号，如 --type BOOLEAN / --type CmsBoolean / --type 7.1.1");
        param(Param.of("fc", null, null, String.class, false), "功能约束 FC，如 --fc ST");
        param(Param.of("cdc", null, null, String.class, false), "公共数据类 CDC，如 --cdc SPS");
        param(Param.of("ln", null, null, String.class, false), "逻辑节点 LN，如 --ln PTOC");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) {
        String v;
        if ((v = args.get("cmd")) != null) { showCommand(v); return; }
        if ((v = args.get("service")) != null) { showService(v); return; }
        if ((v = args.get("type")) != null) { showType(v); return; }
        if ((v = args.get("fc")) != null) { showFc(v); return; }
        if ((v = args.get("cdc")) != null) { showCdc(v); return; }
        if ((v = args.get("ln")) != null) { showLn(v); return; }
        showOverview();
    }

    // ── single command detail ──────────────────────────────────

    private void showCommand(String q) {
        if (isAll(q)) {
            CmsPrinter.error("help --cmd 需要一个命令名，如 --cmd connect");
            return;
        }
        CommandHandler<?, ?> h = console.handlers().get(q.toLowerCase());
        if (h == null) {
            CmsPrinter.error("未知命令: " + q + "（输入 help 查看全部命令）");
            return;
        }
        CmsPrinter.info(h.name() + " — " + h.description() + "  [" + h.requirement() + "]");

        CmsServiceInfo svc = CmsServiceInfo.byName(h.name());
        if (svc != null) {
            CmsPrinter.gray(String.format("  标准: %s  SC=0x%02X  (%s / %s)",
                    svc.section(), svc.serviceCode(), svc.enName(), svc.cnName()));
        }
        for (Param p : h.params()) {
            CmsPrinter.gray(String.format("  --%-10s %-6s 默认=%-8s %s%s",
                    p.cliName(),
                    p.type() == List.class ? "list" : simpleType(p.type()),
                    p.defaultValue() == null ? "-" : p.defaultValue(),
                    p.desp(),
                    p.required() ? "  [必填]" : ""));
        }
    }

    // ── services (CmsServiceInfo) ──────────────────────────────

    private void showService(String q) {
        if (isAll(q)) {
            printServiceTable(servicesInOrder());
            return;
        }
        CmsServiceInfo svc = CmsServiceInfo.byName(q);
        if (svc == null) {
            for (CmsServiceInfo s : CmsServiceInfo.values()) {
                if (q.equals(s.section())) { svc = s; break; }
            }
        }
        if (svc == null) {
            CmsPrinter.error("未找到服务: " + q + "（试试 --service associate 或 --service 8.2.1）");
            return;
        }
        CmsPrinter.info(svc.cliName() + " — " + svc.cnName() + "  (" + svc.section() + ", SC=0x"
                + String.format("%02X", svc.serviceCode()) + ")");
        CmsPrinter.gray("  EN: " + svc.enName());
        CmsPrinter.gray("  " + svc.description());
        if (svc.asn1() != null && !svc.asn1().isEmpty()) {
            CmsPrinter.gray("  ASN.1:");
            for (String line : svc.asn1().split("\n")) {
                CmsPrinter.gray("    " + line);
            }
        }
    }

    // ── data types (CmsDataTypeInfo) ───────────────────────────

    private void showType(String q) {
        if (isAll(q)) {
            printTypeTable(CmsDataTypeInfo.values());
            return;
        }
        CmsDataTypeInfo hit = null;
        for (CmsDataTypeInfo t : CmsDataTypeInfo.values()) {
            if (q.equalsIgnoreCase(t.typeName())
                    || q.equalsIgnoreCase(t.enDescription())
                    || q.equalsIgnoreCase(classNameAlias(t.typeName()))
                    || q.equals(t.section())) {
                hit = t;
                break;
            }
        }
        if (hit == null) {
            CmsPrinter.error("未找到数据类型: " + q + "（试试 --type BOOLEAN / --type CmsBoolean / --type 7.1.1）");
            return;
        }
        CmsPrinter.info(hit.typeName() + " — " + hit.cnDescription() + "  (" + hit.section() + ")");
        CmsPrinter.gray("  EN:   " + hit.enDescription());
        CmsPrinter.gray("  ASN.1: " + hit.asn1Summary());
        CmsPrinter.gray("  类名: " + classNameAlias(hit.typeName()));
    }

    // ── FC (CmsFCInfo) ─────────────────────────────────────────

    private void showFc(String q) {
        if (isAll(q)) {
            printFcTable(CmsFCInfo.values());
            return;
        }
        CmsFCInfo fc = null;
        try {
            fc = CmsFCInfo.fromCode(q.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }
        if (fc == null) {
            CmsPrinter.error("未找到 FC: " + q + "（可选 ST MX SP SV CF DC SG SE SR OR BL EX XX）");
            return;
        }
        CmsPrinter.info(fc.code() + " — " + fc.semantic() + " / " + fc.semanticZh());
        CmsPrinter.gray("  allowed: " + fc.servicesAllowed());
        CmsPrinter.gray("  initial: " + fc.initialValue());
    }

    // ── CDC (CmsCdcInfo) ───────────────────────────────────────

    private void showCdc(String q) {
        if (isAll(q)) {
            printCdcTable(CmsCdcInfo.values());
            return;
        }
        CmsCdcInfo cdc = null;
        for (CmsCdcInfo c : CmsCdcInfo.values()) {
            if (q.equalsIgnoreCase(c.cdcName()) || q.equalsIgnoreCase(c.enName())) { cdc = c; break; }
        }
        if (cdc == null) {
            CmsPrinter.error("未找到 CDC: " + q + "（如 SPS DPS MV SPC INC BRCB ...）");
            return;
        }
        CmsPrinter.info(cdc.cdcName() + " — " + cdc.enName() + " / " + cdc.cnName());
        CmsPrinter.gray("  " + cdc.description());
    }

    // ── LN (CmsLnInfo) ─────────────────────────────────────────

    private void showLn(String q) {
        if (isAll(q)) {
            printLnTable(CmsLnInfo.values());
            return;
        }
        CmsLnInfo ln = null;
        for (CmsLnInfo l : CmsLnInfo.values()) {
            if (q.equalsIgnoreCase(l.lnName())) { ln = l; break; }
        }
        if (ln == null) {
            CmsPrinter.error("未找到 LN: " + q + "（如 PTOC LLN0 PDIS ...）");
            return;
        }
        CmsPrinter.info(ln.lnName() + " — " + ln.enName() + " / " + ln.cnName() + "  [" + ln.lnClass() + "]");
    }

    // ── grouped overview ───────────────────────────────────────

    private void showOverview() {
        Map<String, List<CommandHandler<?, ?>>> groups = new LinkedHashMap<>();
        for (CommandHandler<?, ?> h : console.handlers().values()) {
            if (h.name().equals("help"))
                continue;
            CmsServiceInfo svc = CmsServiceInfo.byName(h.name());
            String key = svc != null ? sectionGroup(svc.section()) : "本地命令";
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(h);
        }
        for (Map.Entry<String, List<CommandHandler<?, ?>>> e : groups.entrySet()) {
            CmsPrinter.info("── " + e.getKey() + " ──");
            for (CommandHandler<?, ?> h : e.getValue()) {
                CmsPrinter.gray(String.format("  %-16s %s", h.name(), h.description()));
            }
            CmsPrinter.gray("");
        }
        CmsPrinter.gray("用法: help --cmd <命令> | --service <名|号> | --type <名|号> | --fc <FC> | --cdc <CDC> | --ln <LN>");
    }

    // ── table printers ─────────────────────────────────────────

    private void printServiceTable(List<CmsServiceInfo> list) {
        CmsPrinter.info("服务表 (命令名 / 章节 / SC / 名称)");
        for (CmsServiceInfo s : list) {
            CmsPrinter.gray(String.format("  %-16s %-6s 0x%02X  %s / %s",
                    s.cliName(), s.section(), s.serviceCode(), s.enName(), s.cnName()));
        }
    }

    private void printTypeTable(CmsDataTypeInfo[] all) {
        CmsPrinter.info("数据类型表 (名称 / 章节 / 编码)");
        for (CmsDataTypeInfo t : all) {
            CmsPrinter.gray(String.format("  %-16s %-6s %s", t.typeName(), t.section(), t.asn1Summary()));
        }
    }

    private void printFcTable(CmsFCInfo[] all) {
        CmsPrinter.info("功能约束 FC 表");
        for (CmsFCInfo fc : all) {
            CmsPrinter.gray(String.format("  %-2s  %-42s %s", fc.code(), fc.semantic(), fc.semanticZh()));
        }
    }

    private void printCdcTable(CmsCdcInfo[] all) {
        CmsPrinter.info("公共数据类 CDC 表");
        for (CmsCdcInfo c : all) {
            CmsPrinter.gray(String.format("  %-5s  %-38s %s", c.cdcName(), c.enName(), c.cnName()));
        }
    }

    private void printLnTable(CmsLnInfo[] all) {
        CmsPrinter.info("逻辑节点 LN 表 (类 / 名称)");
        for (CmsLnInfo l : all) {
            CmsPrinter.gray(String.format("  %-5s  %-42s %s", l.lnName(), l.enName(), l.cnName()));
        }
    }

    // ── helpers ────────────────────────────────────────────────

    private static boolean isAll(String v) {
        return v == null || v.isEmpty() || "true".equals(v);
    }

    /** Order services by section, so the table follows the standard. */
    private static List<CmsServiceInfo> servicesInOrder() {
        List<CmsServiceInfo> list = new ArrayList<>();
        for (CmsServiceInfo s : CmsServiceInfo.values())
            list.add(s);
        list.sort((a, b) -> a.section().compareTo(b.section()));
        return list;
    }

    /** Map a section like "8.2.1" to its group label "关联类服务 (8.2)". */
    private static String sectionGroup(String section) {
        int secondDot = section.indexOf('.', section.indexOf('.') + 1);
        String g = secondDot < 0 ? section : section.substring(0, secondDot);
        return GROUP_LABELS.getOrDefault(g, "其他服务") + " (" + g + ")";
    }

    /** ASN.1 type name → wrapper class name, e.g. "BOOLEAN" → "CmsBoolean". */
    private static String classNameAlias(String typeName) {
        StringBuilder sb = new StringBuilder("Cms");
        for (String part : typeName.toLowerCase().split("_")) {
            if (!part.isEmpty())
                sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    private static String simpleType(Class<?> type) {
        return type.getSimpleName().replace("class ", "");
    }

    private static final Map<String, String> GROUP_LABELS = new LinkedHashMap<>();
    static {
        GROUP_LABELS.put("8.2", "关联类服务");
        GROUP_LABELS.put("8.3", "服务器/逻辑设备/逻辑节点目录");
        GROUP_LABELS.put("8.4", "数据类服务");
        GROUP_LABELS.put("8.5", "数据集服务");
        GROUP_LABELS.put("8.6", "定值组服务");
        GROUP_LABELS.put("8.7", "报告服务");
        GROUP_LABELS.put("8.8", "日志服务");
        GROUP_LABELS.put("8.9", "GOOSE 服务");
        GROUP_LABELS.put("8.10", "多播采样值服务");
        GROUP_LABELS.put("8.11", "控制服务");
        GROUP_LABELS.put("8.12", "文件服务");
        GROUP_LABELS.put("8.13", "远程过程调用");
        GROUP_LABELS.put("8.14", "测试服务");
        GROUP_LABELS.put("8.15", "关联协商服务");
    }
}
