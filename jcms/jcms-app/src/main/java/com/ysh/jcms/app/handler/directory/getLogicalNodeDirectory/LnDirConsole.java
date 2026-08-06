package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.*;

public class LnDirConsole extends CommandHandler {

    public LnDirConsole() {
        super(CommandInfo.LN_DIR);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(
                new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LTSM1）", null), new Param("acsi",
                        "ACSI 类：data-object(1), data-set(2), brcb(3), urcb(4), lcb(5), log(6), sgcb(7), gocb(8), msvcb(10)", "data-object"),
                new Param("after", "起始引用（分页截取）", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ln", "Usage: ln-dir --ln <ldName|lnReference> [--acsi <type>] [--after REF]"))
            return;

        String target = args.get("ln");
        String acsiStr = args.get("acsi").toLowerCase();
        Integer acsi = ACSI_MAP.get(acsiStr);
        if (acsi == null) {
            if (CmsConsole.isJsonMode(args)) {
                CmsConsole.jsonError("Unknown ACSI class: " + args.get("acsi"));
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

        console.getClient(LnDirClient.class).execute(dao);
        List<String> items = new ArrayList<>(console.getContentManager().getNodeRefs(acsi));
        CmsConsole.outputList("References (" + acsiStr + ")", items, s -> s, args);
    }
}
