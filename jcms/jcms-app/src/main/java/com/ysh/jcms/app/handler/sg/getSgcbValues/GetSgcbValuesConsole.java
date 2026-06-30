package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetSgcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "sgcb-vals"; }

    @Override
    public String description() { return "获取定值组控制块值 (GetSGCBValues)。用法: sgcb-vals --refs \"<ref1> <ref2>...\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "SGCB 引用列表（空格分隔），如 \"LD0/LLN0.SG1\"", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --refs. Usage: sgcb-vals --refs \"<ref1> <ref2>...\"");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetSgcbValuesDao dao = new GetSgcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) dao.addRef(ref.trim());
        }

        ConsolePrinter.info("Fetching SGCB values for " + dao.references().size() + " reference(s)");

        console.getClient(GetSgcbValuesClient.class).execute(dao);

        List<GetSgcbValuesClient.SgcbResult> results =
            console.getClient(GetSgcbValuesClient.class).getLastResults();

        if (results.isEmpty()) {
            ConsolePrinter.info("No SGCB values returned");
            return;
        }

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
        ConsolePrinter.list("SGCB values (" + results.size() + " items)",
            display,
            p -> p.ref + "  " + p.detail);
    }

    private static final class RefResultPair {
        final String ref;
        final String detail;
        RefResultPair(String ref, String detail) {
            this.ref = ref; this.detail = detail;
        }
    }
}
