package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.CmsContent;
import com.ysh.jcms.data.scalar.CmsFC;

import java.util.Map;

public class AllDataDefConsole extends CommandHandler<AllDataDefDao, AllDataDefClient> {

    public AllDataDefConsole() {
        super(CommandInfo.ALL_DEF);
        Param p = Param.of("ln", null, null, String.class, true);
        param(p, "ldName 或 lnReference（如 LD0 或 LD0/LLN0）");
        Param p2 = Param.of("fc", "XX", null, String.class, false);
        param(p2, "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤");
        Param p3 = Param.of("after", "", "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ln", "Usage: all-def --ln <ldName|lnReference> [--fc FC] [--after REF]"))
            return;

        String target = args.get("ln");
        AllDataDefDao dao = new AllDataDefDao();
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }

        String fcStr = args.get("fc");
        if (fcStr != null && !fcStr.isEmpty() && !"XX".equalsIgnoreCase(fcStr)) {
            dao.fc(CmsFC.fromString(fcStr));
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty() && !after.equals("0")) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        CmsContent<AllDataDefDao> c = new CmsContent<>(dao, autoPull);
        console.getClient(AllDataDefClient.class).executeResult(c);
        ConsolePrinter.outputJson(c.res());
    }
}