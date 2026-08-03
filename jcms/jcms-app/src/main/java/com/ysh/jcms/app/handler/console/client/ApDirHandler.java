package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.node.SclManager;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 本地配置命令（无需连接）: 列出 SCD 中所有可用的 AccessPoint。
 *
 * <p>
 * DL/T 2811 协议没有"枚举 AP"的服务 —— AP 属于 SCL 配置范畴。因此该命令在 connect 之前直接读取 SCD 文件，输出每个
 * IED 下的 AccessPoint 名，供 {@code connect --ap IED/AP} 使用。
 */
public class ApDirHandler implements CommandHandler {

    @Override
    public String name() {
        return "ap-dir";
    }

    @Override
    public String description() {
        return "列出 SCD 中所有可用的 AccessPoint（本地配置命令，无需连接）。用法: ap-dir [--scd path] [--ied name] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("scd", "SCD 文件路径（默认取配置 server.sclFiles[0]）", ""), new Param("ied", "只列出指定 IED 的 AP（如 C_B5041X）", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);

        // 1) --scd 显式指定 → 优先读 SCD
        String scd = args.get("scd");
        if (scd != null && !scd.isEmpty()) {
            SclManager scl = console.getSclManager();
            scl.load(scd);
            if (!scl.isLoaded() || scl.getDocument() == null) {
                String msg = "SCL 加载失败: " + scd;
                if (jsonMode) {
                    CmsConsole.jsonError(msg);
                } else {
                    ConsolePrinter.error(msg);
                }
                return;
            }
            listFromScd(console, scl, jsonMode, args.get("ied"));
            return;
        }

        // 2) 配置开关: fromScd=false → 直接输出 defaultAps 静态列表
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().getClient().getAccessPoint();
        if (!apCfg.isFromScd()) {
            listFromDefault(apCfg.getDefaultAps(), jsonMode);
            return;
        }

        // 3) fromScd=true（默认）→ 读配置里的 SCD 文件
        SclManager scl = console.getSclManager();
        String cfg = CmsConfigLoader.load().getServer().getResolvedSclFile();
        if (cfg != null && !cfg.isEmpty()) {
            scl.load(cfg);
        }
        if (!scl.isLoaded() || scl.getDocument() == null) {
            String msg = "SCL 未加载。请用 --scd <path> 指定 SCD 文件，或在配置中设置 server.sclFiles。";
            if (jsonMode) {
                CmsConsole.jsonError(msg);
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }
        listFromScd(console, scl, jsonMode, args.get("ied"));
    }

    private void listFromDefault(List<String> defaultAps, boolean jsonMode) {
        if (defaultAps == null || defaultAps.isEmpty()) {
            String msg = "defaultAps 列表为空。请在配置 client.accessPoint.defaultAps 中添加 AP 引用。";
            if (jsonMode) {
                CmsConsole.jsonError(msg);
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }
        if (jsonMode) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < defaultAps.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('"').append(defaultAps.get(i)).append('"');
            }
            CmsConsole.jsonArray(sb.toString());
            return;
        }
        ConsolePrinter.info("AP 列表（client.accessPoint.defaultAps）");
        ConsolePrinter.list("AccessPoint", defaultAps, s -> s);
    }

    private void listFromScd(CmsConsole console, SclManager scl, boolean jsonMode, String iedFilter) throws Exception {
        SclDocument doc = scl.getDocument();
        List<SclIED> ieds = doc.ieds();
        if (iedFilter != null && !iedFilter.isEmpty()) {
            SclIED target = doc.ied(iedFilter);
            if (target == null) {
                String msg = "IED 不存在: " + iedFilter;
                if (jsonMode) {
                    CmsConsole.jsonError(msg);
                } else {
                    ConsolePrinter.error(msg);
                }
                return;
            }
            ieds = new ArrayList<>(Collections.singletonList(target));
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ieds.size(); i++) {
                SclIED ied = ieds.get(i);
                if (i > 0) {
                    sb.append(',');
                }
                List<String> aps = ied.accessPoints().stream().map(SclAccessPoint::name).collect(Collectors.toList());
                sb.append("{\"ied\":\"").append(ied.name()).append("\",\"aps\":[");
                for (int j = 0; j < aps.size(); j++) {
                    if (j > 0) {
                        sb.append(',');
                    }
                    sb.append('"').append(aps.get(j)).append('"');
                }
                sb.append("]}");
            }
            CmsConsole.jsonArray(sb.toString());
            return;
        }

        ConsolePrinter.info("SCL: " + scl.getSource());
        for (SclIED ied : ieds) {
            ConsolePrinter.list("IED " + ied.name(), ied.accessPoints(), SclAccessPoint::name);
        }
    }
}
