package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;

import java.util.*;

public class LnDirConsole extends CommandHandler {

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
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LTSM1）", null),
                new Param("acsi", "ACSI 类：data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)",
                        "data-object"),
                new Param("after", "起始引用（分页截取）", ""), new Param("auto-pull", "自动续拉分页（true/false）", "false"),
                new Param("json", "JSON 格式输出", ""));
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
            if (CmsConsole.isJsonMode(args)) {
                ConsolePrinter.error("Unknown ACSI class: " + args.get("acsi"));
            } else {
                ConsolePrinter.error("Unknown ACSI class: " + args.get("acsi")
                        + ". Available: data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)");
            }
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
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        console.getClient(LnDirClient.class).execute(dao);
        LnDirContext ctx = (LnDirContext) dao.paginationContext();
        boolean moreFollows = ctx.isLastMoreFollows();
        List<String> items = new ArrayList<>(ctx.getAccumulatedRefs());
        CmsConsole.outputList("References (" + acsiStr + ")", items, s -> s, args, moreFollows);
    }
}
