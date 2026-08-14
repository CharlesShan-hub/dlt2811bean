package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.conformance.SclConformanceCheck;
import com.ysh.jcms.utils.scl.conformance.SclConformanceIssue;
import com.ysh.jcms.utils.scl.conformance.SclConformanceMode;
import com.ysh.jcms.utils.scl.reader.SclReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地配置命令（无需连接）: 按 Q/GDW 1396-2012《IEC 61850 工程继电保护应用模型》校验 SCL 文件。
 *
 * <p>
 * 校验基于已解析的完整 {@link SclDocument} 模型（比 ap-dir 的轻量扫描重，适合中小文件按需检查）。 默认模式取配置
 * scl.conformanceMode（未配置或为 LOOSE 时需显式 --mode strict 才执行国网检查）， 也可用 --mode loose
 * 显式跳过国网规则。
 */
public class ConformanceHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ConformanceHandler() {
        super(CommandInfo.SCL_CHECK);
        Param p1 = Param.of("scd", null, null, String.class, false);
        param(p1, "SCD/ICD 文件路径（默认取配置 server.sclFiles[0]）");
        Param p2 = Param.of("mode", null, null, String.class, false);
        param(p2, "校验模式: strict=国网 Q/GDW 1396 / loose=跳过（默认取配置 scl.conformanceMode）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String scd = args.get("scd");
        if (scd == null || scd.isEmpty()) {
            scd = CmsConfigLoader.load().server().getResolvedSclFile();
        }
        if (scd == null || scd.isEmpty()) {
            CmsPrinter.error("SCL 未加载。请用 --scd <path> 指定 SCD 文件，或在配置中设置 server.sclFiles。");
            return;
        }

        String cfgMode = CmsConfigLoader.load().scl().conformanceMode();
        String modeArg = args.get("mode");
        String modeName = modeArg != null && !modeArg.isEmpty() ? modeArg : cfgMode;
        SclConformanceMode mode = SclConformanceMode.from(modeName);

        SclDocument doc;
        try {
            doc = new SclReader().read(scd);
        } catch (Exception e) {
            CmsPrinter.error("SCL 解析失败: " + scd + " - " + e.getMessage());
            return;
        }

        List<SclConformanceIssue> issues = SclConformanceCheck.check(doc, mode);

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("scd", scd);
        data.put("mode", mode.name());
        data.put("total", issues.size());

        LinkedHashMap<String, Object> counts = new LinkedHashMap<>();
        int errors = 0;
        int warns = 0;
        int infos = 0;
        for (SclConformanceIssue issue : issues) {
            switch (issue.severity()) {
                case ERROR :
                    errors++;
                    break;
                case WARN :
                    warns++;
                    break;
                default :
                    infos++;
                    break;
            }
        }
        counts.put("errors", errors);
        counts.put("warnings", warns);
        counts.put("infos", infos);
        data.put("severity", counts);

        List<Map<String, Object>> items = new ArrayList<>();
        for (SclConformanceIssue issue : issues) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<>();
            item.put("severity", issue.severity().name());
            item.put("category", issue.category());
            item.put("clause", issue.clause());
            item.put("ref", issue.ref());
            item.put("message", issue.message());
            items.add(item);
        }
        data.put("issues", items);

        CmsPrinter.outputJson(data);
    }
}
