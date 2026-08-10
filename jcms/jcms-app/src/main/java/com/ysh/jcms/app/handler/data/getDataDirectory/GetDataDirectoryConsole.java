package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataDirectoryConsole extends CommandHandler {

    public GetDataDirectoryConsole() {
        super(CommandInfo.DATA_DIR);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "数据引用，如 LD0/LLN0 或 LD0/LLN0.Mod", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: data-dir --ref <ref> [--after REF]"))
            return;

        String ref = args.get("ref");
        GetDataDirectoryDao dao = new GetDataDirectoryDao().dataReference(ref.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        if (!CmsConsole.isJsonMode(args)) {
            ConsolePrinter.info("Fetching data directory for " + ref);
        }

        console.getClient(GetDataDirectoryClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();

        boolean moreFollows = ctx.isLastMoreFollows();
        @SuppressWarnings("unchecked")
        List<GetDataDirectoryClient.DirEntry> entries = (List<GetDataDirectoryClient.DirEntry>) ctx.getResult();
        if (entries == null) {
            entries = java.util.Collections.emptyList();
        }

        if (entries.isEmpty()) {
            if (CmsConsole.isJsonMode(args)) {
                ConsolePrinter.raw("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[]}");
            } else {
                ConsolePrinter.info("No data directory entries");
            }
            return;
        }

        CmsConsole.outputList("Data directory (" + entries.size() + " items)", new java.util.ArrayList<>(entries), e -> {
            if (e.fc != null)
                return "[" + e.fc + "]  " + e.reference;
            return e.reference;
        }, args, moreFollows);
    }
}
