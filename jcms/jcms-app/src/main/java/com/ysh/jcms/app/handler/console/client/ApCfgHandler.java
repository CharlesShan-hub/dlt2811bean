package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler for the {@code ap-cfg} command.
 *
 * <p>
 * 运行时查看/修改 AP 来源配置（对齐 trace-pdu 的内存开关模式，改完即生效， 无需改 yaml 重启）。影响 {@code ap-dir}
 * 的读取来源。
 */
public class ApCfgHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ApCfgHandler() {
        super(CommandInfo.AP_CFG);
        Param p = Param.of("source", null, null, String.class, false);
        param(p, "AP 来源: scd=从 SCD 读, list=从 defaultAps 列表读");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        CmsConfig.Client.AccessPoint apCfg = CmsConfigLoader.load().client().accessPoint();

        // --source 只设置来源，输出简洁确认
        String source = args.get("source");
        if (source != null && !source.isEmpty()) {
            if ("scd".equalsIgnoreCase(source)) {
                apCfg.fromScd(true);
            } else if ("list".equalsIgnoreCase(source)) {
                apCfg.fromScd(false);
            } else {
                CmsPrinter.error("无效的 source: " + source + "（可选 scd|list）");
                return;
            }
            CmsPrinter.success("AP 来源已设为: " + (apCfg.fromScd() ? "scd" : "list"));
            return;
        }

        // 无参数 → 查看当前来源（即上一次设置的结果）
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("fromScd", apCfg.fromScd());
        data.put("defaultAps", apCfg.defaultAps());
        CmsPrinter.result(data);
    }
}
