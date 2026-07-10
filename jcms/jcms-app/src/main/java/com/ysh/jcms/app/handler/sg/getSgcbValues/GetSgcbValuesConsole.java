package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetSgcbValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "sgcb-vals";
    }

    @Override
    public String description() {
        return "获取定值组控制块值 (GetSGCBValues)。用法: sgcb-vals --refs \"<ref1> <ref2>...\" [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "SGCB 引用列表（空格分隔），如 \"LD0/LLN0.SG1\"", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            String msg = "Not connected. Type 'connect' first.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            String msg = "Missing --refs. Usage: sgcb-vals --refs \"<ref1> <ref2>...\"";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetSgcbValuesDao dao = new GetSgcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim());
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching SGCB values for " + dao.references().size() + " reference(s)");
        }

        console.getClient(GetSgcbValuesClient.class).execute(dao);

        List<GetSgcbValuesClient.SgcbResult> results = console.getClient(GetSgcbValuesClient.class).getLastResults();

        if (results.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No SGCB values returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < results.size(); i++) {
                GetSgcbValuesClient.SgcbResult r = results.get(i);
                String ref = i < refs.length ? refs[i] : "#" + i;
                if (i > 0)
                    sb.append(',');
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\"");
                sb.append(",\"success\":").append(r.success);
                sb.append(",\"numOfSG\":").append(r.numOfSG);
                sb.append(",\"actSG\":").append(r.actSG);
                sb.append(",\"editSG\":").append(r.editSG);
                sb.append("}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            List<RefResultPair> display = new java.util.ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                GetSgcbValuesClient.SgcbResult r = results.get(i);
                String ref = i < refs.length ? refs[i] : "#" + i;
                String detail;
                if (r.success) {
                    detail = "numOfSG=" + r.numOfSG + " actSG=" + r.actSG + " editSG=" + r.editSG;
                } else {
                    detail = "(error)";
                }
                display.add(new RefResultPair(ref, detail));
            }
            ConsolePrinter.list("SGCB values (" + results.size() + " items)", display, p -> p.ref + "  " + p.detail);
        }
    }

    private static final class RefResultPair {
        final String ref;
        final String detail;
        RefResultPair(String ref, String detail) {
            this.ref = ref;
            this.detail = detail;
        }
    }
}
