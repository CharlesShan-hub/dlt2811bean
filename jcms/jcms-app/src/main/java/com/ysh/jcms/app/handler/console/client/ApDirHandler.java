package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.reader.SclReader;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地配置命令（无需连接）: 列出 SCD 中所有可用的 AccessPoint。
 *
 * <p>
 * DL/T 2811 协议没有"枚举 AP"的服务 —— AP 属于 SCL 配置范畴。因此该命令在 connect 之前直接读取 SCD 文件，输出每个
 * IED 下的 AccessPoint 名，供 {@code connect --ap IED/AP} 使用。
 *
 * <p>
 * 使用 {@link SclReader#scanAccessPoints} 轻量扫描，只读取 IED/AccessPoint 名称属性，不构建完整 SCL
 * 模型，因此大 SCD（几百个 IED）也能秒级返回。
 */
public class ApDirHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ApDirHandler() {
        super(CommandInfo.AP_DIR);
        Param p1 = Param.of("scd", null, null, String.class, false);
        param(p1, "SCD 文件路径（默认取配置 server.sclFiles[0]）");
        Param p2 = Param.of("ied", null, null, String.class, false);
        param(p2, "只列出指定 IED 的 AP（如 C_B5041X）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        // 解析 SCL：优先 --scd，其次配置里的 server.sclFiles
        String scd = args.get("scd");
        if (scd == null || scd.isEmpty()) {
            scd = CmsConfigLoader.load().server().getResolvedSclFile();
        }
        if (scd == null || scd.isEmpty()) {
            CmsPrinter.error("SCL 未加载。请用 --scd <path> 指定 SCD 文件，或在配置中设置 server.sclFiles。");
            return;
        }

        Map<String, List<String>> apsByIed;
        try {
            // 轻量扫描：只取 IED/AccessPoint 名称，大文件也秒级完成
            apsByIed = SclReader.scanAccessPoints(Paths.get(scd));
        } catch (Exception e) {
            CmsPrinter.error("SCL 解析失败: " + scd + " - " + e.getMessage());
            return;
        }

        // --ied 过滤
        String iedFilter = args.get("ied");
        if (iedFilter != null && !iedFilter.isEmpty()) {
            if (!apsByIed.containsKey(iedFilter)) {
                CmsPrinter.error("IED 不存在: " + iedFilter);
                return;
            }
            apsByIed = new LinkedHashMap<>(Collections.singletonMap(iedFilter, apsByIed.get(iedFilter)));
        }

        // 拼出 IED/AP 完整引用
        List<String> refs = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : apsByIed.entrySet()) {
            for (String ap : e.getValue()) {
                refs.add(e.getKey() + "/" + ap);
            }
        }

        // 输出 JSON
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("scd", scd);
        data.put("accessPoints", refs);
        CmsPrinter.outputJson(data);
    }
}
