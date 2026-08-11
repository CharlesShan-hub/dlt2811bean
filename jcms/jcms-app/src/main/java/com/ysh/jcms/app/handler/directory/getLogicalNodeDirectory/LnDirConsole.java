package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.LinkedHashMap;
import java.util.Map;

public class LnDirConsole extends CommandHandler<LnDirDao, LnDirClient> {

    private static final Map<String, String> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("data-object", "1");
        ACSI_MAP.put("data-set", "2");
        ACSI_MAP.put("brcb", "3");
        ACSI_MAP.put("urcb", "4");
        ACSI_MAP.put("lcb", "5");
        ACSI_MAP.put("log", "6");
        ACSI_MAP.put("sgcb", "7");
        ACSI_MAP.put("gocb", "8");
        ACSI_MAP.put("msvcb", "10");
    }

    public LnDirConsole() {
        super(CommandInfo.LN_DIR);
        Param p = Param.of("ln", null, "ln", String.class, true);
        param(p, "逻辑节点引用，如 LD0 或 LD0/LTSM1");
        Param p2 = Param.of("acsi", "data-object", "acsiClass", Integer.class, false).valueMap(ACSI_MAP);
        param(p2, "ACSI 类型：data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)");
        Param p3 = Param.of("after", "", "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取，不传则从头开始）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }
}
