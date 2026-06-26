package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;
import com.ysh.jcms.svc.directory.CmsAcsiClass;

import java.util.*;

public class LnDirCli implements CommandHandler {

    private static final Map<String, Integer> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("data-object", CmsAcsiClass.DATA_OBJECT);
        ACSI_MAP.put("data-set", CmsAcsiClass.DATA_SET);
        ACSI_MAP.put("brcb", CmsAcsiClass.BRCB);
        ACSI_MAP.put("urcb", CmsAcsiClass.URCB);
        ACSI_MAP.put("lcb", CmsAcsiClass.LCB);
        ACSI_MAP.put("gocb", CmsAcsiClass.GOCB);
        ACSI_MAP.put("msvcb", CmsAcsiClass.MSVCB);
    }

    @Override
    public String name() { return "ln-dir"; }

    @Override
    public String description() { return "获取逻辑节点子目录 (GetLogicalNodeDirectory)"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("target", "ldName 或 lnReference", "C1"),
            new Param("acsi", "ACSI 类", "data-object")
        );
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { CliPrinter.error("Not connected. Type 'connect' first."); return; }
        String target = args.get("target");
        String acsiStr = args.get("acsi").toLowerCase();
        Integer acsi = ACSI_MAP.get(acsiStr);
        if (acsi == null) {
            CliPrinter.error("Unknown ACSI class: " + args.get("acsi"));
            return;
        }

        LnDirDao dao = new LnDirDao()
            .acsiClass(acsi);
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }

        ctx.node().getClient(LnDirClient.class).execute(dao);
        CliPrinter.list("References (" + acsiStr + ")",
            new ArrayList<>(ctx.node().getContentManager().getNodeRefs(acsi)),
            s -> s);
    }
}
