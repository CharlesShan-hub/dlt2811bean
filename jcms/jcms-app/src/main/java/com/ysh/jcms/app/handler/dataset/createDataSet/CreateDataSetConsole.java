package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.data.scalar.CmsFC;

import java.util.Map;

public class CreateDataSetConsole extends CommandHandler<CreateDataSetDao, CreateDataSetClient> {

    public CreateDataSetConsole() {
        super(CommandInfo.CREATE_DATASET);
        param("ds", "数据集引用，如 \"LD0/LLN0.myDs\"", null);
        param("members", "成员列表（空格分隔，每个成员的格式 ref,fc），如 \"LD0/GGIO1.Alm1,ST LD0/GGIO1.Alm2,ST\"", null);
        param("after", "追加到现有数据集后的最后一个成员引用", "");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ds", "Usage: create-dataset --ds <ref> --members \"...\""))
            return;
        if (!CmsConsole.requireParam(args, "members", "Usage: create-dataset --ds <ref> --members \"...\""))
            return;

        String dsRef = args.get("ds");
        String membersStr = args.get("members");

        CreateDataSetDao dao = new CreateDataSetDao().datasetReference(dsRef.trim());

        String[] tokens = membersStr.trim().split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty())
                continue;
            int commaIdx = token.indexOf(',');
            if (commaIdx <= 0) {
                ConsolePrinter.error("Invalid member: " + token + " (expected ref,fc)");
                return;
            }
            String ref = token.substring(0, commaIdx);
            String fcStr = token.substring(commaIdx + 1);
            int fcCode = CmsFC.fromString(fcStr);
            dao.addMember(ref, fcCode);
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        ConsolePrinter.info("Creating dataset " + dsRef + " with " + dao.members().size() + " member(s)");

        console.getClient(CreateDataSetClient.class).execute(dao);

        ConsolePrinter.success("Created dataset " + dsRef + " successfully");
    }
}
