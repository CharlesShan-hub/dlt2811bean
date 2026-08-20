package com.ysh.jcms.app.handler.console.ap;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.reader.SclReader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Client-side {@code ap} command: enumerate access points or manage the AP source config.
 *
 * <p>
 * The client may not have the SCD file locally, so the enumeration source is
 * pluggable: {@code scd} (lightweight scan of an SCD file) or {@code list}
 * (cached {@code defaultAps} from config). The default source comes from
 * {@code ap --cfg --source}; when the SCD file is unavailable, {@code ap --list}
 * falls back to the cached list automatically.
 *
 * <p>
 * Examples:
 * <pre>
 *   ap                          # list APs (source auto: scd, falls back to list)
 *   ap --list --source list     # list APs from the cached defaultAps list
 *   ap --list --source scd --scd model.scd --ied C_B5041X   # scan a specific SCD, filter by IED
 *   ap --cfg                    # show current source config and cached list
 *   ap --cfg --source list      # switch default source to the cached list
 *   ap --cfg --add C_B5041X/S1  # add a reference to the cached list
 *   ap --cfg --rm C_B5041X/S1   # remove a reference from the cached list
 * </pre>
 */
public class ApClientHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ApClientHandler() {
        super(CommandInfo.AP);
        Param p1 = Param.of("list", null, null, String.class, false);
        param(p1, "列出访问点（默认行为）");
        Param p2 = Param.of("cfg", null, null, String.class, false);
        param(p2, "查看/修改 AP 来源配置");
        Param p3 = Param.of("source", null, null, String.class, false);
        param(p3, "来源: scd=读 SCD 文件, list=读缓存列表（默认取配置 fromScd）");
        Param p4 = Param.of("scd", null, null, String.class, false);
        param(p4, "SCD 文件路径（默认取配置 server.sclFiles[0]）");
        Param p5 = Param.of("ied", null, null, String.class, false);
        param(p5, "只列出指定 IED 的 AP");
        Param p6 = Param.of("add", null, null, String.class, false);
        param(p6, "往缓存列表加一个引用（--cfg）");
        Param p7 = Param.of("rm", null, null, String.class, false);
        param(p7, "从缓存列表删除一个引用（--cfg）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if ("true".equals(args.get("cfg"))) {
            config(args);
            return;
        }
        list(args);
    }

    // ── ap --cfg ─────────────────────────────────────────────

    private void config(Map<String, String> args) {
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().client().accessPoint();
        boolean changed = false;

        String source = args.get("source");
        if (source != null) {
            if ("scd".equalsIgnoreCase(source)) {
                apCfg.fromScd(true);
                changed = true;
            } else if ("list".equalsIgnoreCase(source)) {
                apCfg.fromScd(false);
                changed = true;
            } else {
                CmsPrinter.error("无效的 source: " + source + "（可选 scd|list）");
                return;
            }
        }

        String add = args.get("add");
        if (add != null && !add.isEmpty()) {
            if (!apCfg.defaultAps().contains(add)) {
                apCfg.defaultAps().add(add);
            }
            changed = true;
        }

        String rm = args.get("rm");
        if (rm != null && !rm.isEmpty()) {
            apCfg.defaultAps().remove(rm);
            changed = true;
        }

        if (changed) {
            CmsPrinter.success("AP 配置已更新: source=" + (apCfg.fromScd() ? "scd" : "list"));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("fromScd", apCfg.fromScd());
        data.put("defaultAps", apCfg.defaultAps());
        CmsPrinter.result(data);
    }

    // ── ap --list ────────────────────────────────────────────

    private void list(Map<String, String> args) {
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().client().accessPoint();
        String sourceArg = args.get("source");
        boolean explicit = sourceArg != null;
        String source = explicit ? sourceArg.toLowerCase() : (apCfg.fromScd() ? "scd" : "list");

        if (!"scd".equals(source) && !"list".equals(source)) {
            CmsPrinter.error("无效的 source: " + sourceArg + "（可选 scd|list）");
            return;
        }

        if ("scd".equals(source)) {
            if (tryListFromScd(args)) {
                return;
            }
            if (explicit) {
                return; // error already printed
            }
            // Configured source is scd but no usable SCD file → fall back to the cached list
            CmsPrinter.gray("SCD unavailable, falling back to cached list (defaultAps).");
            listFromConfig();
            return;
        }
        listFromConfig();
    }

    private boolean tryListFromScd(Map<String, String> args) {
        String scd = args.get("scd");
        if (scd == null || scd.isEmpty()) {
            scd = CmsConfigLoader.load().server().getResolvedSclFile();
        }
        if (scd == null || scd.isEmpty()) {
            CmsPrinter.error("SCL 未加载。请用 --scd <path> 指定 SCD 文件，或在配置中设置 server.sclFiles。");
            return false;
        }
        if (!Files.exists(Paths.get(scd))) {
            CmsPrinter.error("SCD 文件不存在: " + scd);
            return false;
        }

        Map<String, List<String>> apsByIed;
        try {
            apsByIed = SclReader.scanAccessPoints(Paths.get(scd));
        } catch (Exception e) {
            CmsPrinter.error("SCL 解析失败: " + scd + " - " + e.getMessage());
            return false;
        }

        String iedFilter = args.get("ied");
        if (iedFilter != null && !iedFilter.isEmpty()) {
            if (!apsByIed.containsKey(iedFilter)) {
                CmsPrinter.error("IED 不存在: " + iedFilter);
                return false;
            }
            apsByIed = new LinkedHashMap<>(Collections.singletonMap(iedFilter, apsByIed.get(iedFilter)));
        }

        List<String> refs = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : apsByIed.entrySet()) {
            for (String ap : e.getValue()) {
                refs.add(e.getKey() + "/" + ap);
            }
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("source", "scd");
        data.put("scd", scd);
        data.put("accessPoints", refs);
        CmsPrinter.result(data);
        return true;
    }

    private void listFromConfig() {
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().client().accessPoint();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("source", "list");
        data.put("accessPoints", apCfg.defaultAps());
        CmsPrinter.result(data);
    }
}
