package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.node.SclManager;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handler for the {@code ap-cfg} command.
 *
 * <p>
 * 运行时查看/修改 AP 来源配置（对齐 trace-pdu 的内存开关模式，改完即生效， 无需改 yaml 重启）。影响 {@code ap-dir}
 * 的读取来源。
 */
public class ApCfgHandler implements CommandHandler {

    @Override
    public String name() {
        return "ap-cfg";
    }

    @Override
    public String description() {
        return "查看/修改 AP 来源配置（运行时生效）。用法: ap-cfg [--source scd|list] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("source", "AP 来源: scd=从 SCD 读, list=从 defaultAps 列表读", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().getClient().getAccessPoint();

        // --source 只设置来源，输出简洁确认
        String source = args.get("source");
        if (source != null && !source.isEmpty()) {
            if ("scd".equalsIgnoreCase(source)) {
                apCfg.setFromScd(true);
            } else if ("list".equalsIgnoreCase(source)) {
                apCfg.setFromScd(false);
            } else {
                String msg = "无效的 source: " + source + "（可选 scd|list）";
                if (jsonMode) {
                    CmsConsole.jsonError(msg);
                } else {
                    ConsolePrinter.error(msg);
                }
                return;
            }
            if (jsonMode) {
                CmsConsole.jsonMessage("AP 来源已设为: " + (apCfg.isFromScd() ? "scd" : "list"));
            } else {
                ConsolePrinter.success("AP 来源已设为: " + (apCfg.isFromScd() ? "scd" : "list"));
            }
            return;
        }

        // 无参数 → 查看当前来源（即上一次设置的结果）
        if (jsonMode) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"fromScd\":").append(apCfg.isFromScd()).append(",\"defaultAps\":[");
            List<String> aps = apCfg.getDefaultAps();
            for (int i = 0; i < aps.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('"').append(CmsFormatUtil.escapeJson(aps.get(i))).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw("{\"success\":true,\"data\":" + sb + "}");
            return;
        }

        ConsolePrinter.info("AP 来源: " + (apCfg.isFromScd() ? "scd（从 SCD 文件读）" : "list（从 defaultAps 列表读）"));
        if (apCfg.isFromScd()) {
            String scl = CmsConfigLoader.load().getServer().getResolvedSclFile();
            if (scl != null && !scl.isEmpty()) {
                ConsolePrinter.info("SCL: " + scl);
                // 解析 SCD，拼出 IED/AP 完整引用列表
                SclManager sm = console.getSclManager();
                sm.load(scl);
                if (sm.isLoaded() && sm.getDocument() != null) {
                    List<String> refs = new ArrayList<>();
                    for (SclIED ied : sm.getDocument().ieds()) {
                        for (SclAccessPoint ap : ied.accessPoints()) {
                            refs.add(ied.name() + "/" + ap.name());
                        }
                    }
                    ConsolePrinter.listItems(refs, s -> s);
                } else {
                    ConsolePrinter.error("SCL 解析失败: " + scl);
                }
            } else {
                ConsolePrinter.info("SCL: （未配置 server.sclFiles）");
            }
        } else {
            ConsolePrinter.listItems(apCfg.getDefaultAps(), s -> s);
        }
    }
}
