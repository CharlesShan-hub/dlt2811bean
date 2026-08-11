package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.CmsContent;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;

import java.util.*;

public class LnDirConsole extends CommandHandler<LnDirDao, LnDirClient> {

    private static final Map<String, Integer> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("data-object", CmsAcsiClass.DATA_OBJECT);
        ACSI_MAP.put("data-set", CmsAcsiClass.DATA_SET);
        ACSI_MAP.put("brcb", CmsAcsiClass.BRCB);
        ACSI_MAP.put("urcb", CmsAcsiClass.URCB);
        ACSI_MAP.put("lcb", CmsAcsiClass.LCB);
        ACSI_MAP.put("gocb", CmsAcsiClass.GOCB);
        ACSI_MAP.put("msvcb", CmsAcsiClass.MSVCB);
        ACSI_MAP.put("log", CmsAcsiClass.LOG);
        ACSI_MAP.put("sgcb", CmsAcsiClass.SGCB);
        // integer strings
        ACSI_MAP.put("1", CmsAcsiClass.DATA_OBJECT);
        ACSI_MAP.put("2", CmsAcsiClass.DATA_SET);
        ACSI_MAP.put("3", CmsAcsiClass.BRCB);
        ACSI_MAP.put("4", CmsAcsiClass.URCB);
        ACSI_MAP.put("5", CmsAcsiClass.LCB);
        ACSI_MAP.put("6", CmsAcsiClass.LOG);
        ACSI_MAP.put("7", CmsAcsiClass.SGCB);
        ACSI_MAP.put("8", CmsAcsiClass.GOCB);
        ACSI_MAP.put("10", CmsAcsiClass.MSVCB);
    }

    public LnDirConsole() {
        super(CommandInfo.LN_DIR);
        Param p = Param.of("ln", null, null, String.class, true);
        param(p, "逻辑节点引用，如 LD0 或 LD0/LTSM1");
        Param p2 = Param.of("acsi", "data-object", null, String.class, false);
        param(p2, "ACSI 类型：data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)");
        Param p3 = Param.of("after", "", "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取，不传则从头开始）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ln", "Usage: ln-dir --ln <ldName|lnReference> [--acsi <type>] [--after REF]"))
            return;

        String target = args.get("ln");
        String acsiStr = args.get("acsi").toLowerCase();
        Integer acsi = ACSI_MAP.get(acsiStr);
        if (acsi == null) {
            ConsolePrinter.error("Unknown ACSI class: " + args.get("acsi")
                    + ". Available: data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)");
            return;
        }

        LnDirDao dao = new LnDirDao().acsiClass(acsi);
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        CmsContent<LnDirDao> c = new CmsContent<>(dao, autoPull);
        console.getClient(LnDirClient.class).executeResult(c);
        ConsolePrinter.outputJson(c.res());
    }
}