package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;
import com.ysh.jcms.data.fc.CmsFC;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetEditSgValueConsole implements CommandHandler {

    private static final String[] CHOICE_NAMES = {"error", "array", "structure", "boolean", "int8", "int16", "int32", "int64", "int8u",
            "int16u", "int32u", "int64u", "float32", "float64", "bit-string", "octet-string", "visible-string", "unicode-string",
            "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"};

    @Override
    public String name() {
        return "get-edit-sg";
    }

    @Override
    public String description() {
        return "获取编辑定值组值 (GetEditSGValue)。用法: get-edit-sg --refs \"<ref1> <ref2>...\" --fc SG/SE [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "数据引用列表（空格分隔），如 LD0/LLN0.Mod LD0/LLN0.Beh", null),
                new Param("fc", "功能约束（SG 或 SE），默认 SG", "SG"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "refs", "Missing --refs"))
            return;

        String refsStr = args.get("refs");
        String fcStr = args.get("fc");
        if (fcStr == null)
            fcStr = "SG";

        int fcCode = CmsFC.fromString(fcStr);

        String[] refs = refsStr.trim().split("\\s+");
        GetEditSgValueDao dao = new GetEditSgValueDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim(), fcCode);
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching edit SG values (" + fcStr + ") for " + dao.refs().size() + " ref(s)");
        }

        console.getClient(GetEditSgValueClient.class).execute(dao);

        List<GetEditSgValueClient.ValueEntry> values = console.getClient(GetEditSgValueClient.class).getLastValues();

        if (values.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No values returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < values.size(); i++) {
                GetEditSgValueClient.ValueEntry v = values.get(i);
                String ref = i < refs.length ? refs[i] : "#" + i;
                String typeName = v.choice >= 0 && v.choice < CHOICE_NAMES.length ? CHOICE_NAMES[v.choice] : "?";
                if (i > 0)
                    sb.append(',');
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\"");
                sb.append(",\"type\":\"").append(CmsFormatUtil.escapeJson(typeName)).append("\"");
                sb.append(",\"value\":\"").append(CmsFormatUtil.escapeJson(v.text)).append("\"");
                sb.append("}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            List<RefValPair> display = new java.util.ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                GetEditSgValueClient.ValueEntry v = values.get(i);
                String ref = i < refs.length ? refs[i] : "#" + i;
                String typeName = v.choice >= 0 && v.choice < CHOICE_NAMES.length ? CHOICE_NAMES[v.choice] : "?";
                display.add(new RefValPair(ref, "[" + typeName + "] " + v.text));
            }
            ConsolePrinter.list("Edit SG values (" + values.size() + " items)", display, p -> p.ref + "  " + p.val);
        }
    }

    private static final class RefValPair {
        final String ref;
        final String val;
        RefValPair(String ref, String val) {
            this.ref = ref;
            this.val = val;
        }
    }
}
