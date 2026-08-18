package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.LinkedHashMap;
import java.util.Map;

public class AllCbValuesConsole extends CommandHandler<AllCbValuesDao, AllCbValuesClient> {

    private static final Map<String, String> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("brcb", "3");
        ACSI_MAP.put("urcb", "4");
        ACSI_MAP.put("lcb", "5");
        ACSI_MAP.put("sgcb", "7");
        ACSI_MAP.put("gocb", "8");
        ACSI_MAP.put("msvcb", "10");
    }

    public AllCbValuesConsole() {
        super(CommandInfo.ALL_CB);
        Param p = Param.of("ln", null, "reference", String.class, true);
        param(p, "ldName 或 lnReference（如 LD0 或 LD0/LLN0）");
        Param p2 = Param.of("acsi", null, "acsiClass", int.class, true).valueMap(ACSI_MAP);
        param(p2, "ACSI 类型: brcb(3)/urcb(4)/lcb(5)/sgcb(7)/gocb(8)/msvcb(10)");
        Param p3 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }
}
