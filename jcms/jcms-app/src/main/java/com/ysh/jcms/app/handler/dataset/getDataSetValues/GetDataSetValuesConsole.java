package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ysh.jcms.data.choice.CmsData;

public class GetDataSetValuesConsole extends CommandHandler {

    public GetDataSetValuesConsole() {
        super(CommandInfo.GET_DATASET_VALUES);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ds", "Usage: get-dataset-values --ds <ref> [--after REF]"))
            return;

        String dsRef = args.get("ds");

        GetDataSetValuesDao dao = new GetDataSetValuesDao().datasetReference(dsRef.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        console.getClient(GetDataSetValuesClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();

        @SuppressWarnings("unchecked")
        List<GetDataSetValuesClient.DataSetValue> values = (List<GetDataSetValuesClient.DataSetValue>) ctx.getResult();
        if (values == null) {
            values = java.util.Collections.emptyList();
        }

        if (values.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0)
                sb.append(',');
            GetDataSetValuesClient.DataSetValue v = values.get(i);
            String typeName = v.choiceType >= 0 && v.choiceType < CmsData.CHOICE_NAMES.length ? CmsData.CHOICE_NAMES[v.choiceType] : "?";
            sb.append("{\"type\":\"").append(CmsFormatUtil.escapeJson(typeName)).append("\",\"value\":\"")
                    .append(CmsFormatUtil.escapeJson(v.valueString)).append("\"}");
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}
