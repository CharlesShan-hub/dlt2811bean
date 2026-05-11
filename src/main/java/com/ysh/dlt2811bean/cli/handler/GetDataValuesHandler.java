package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.FcInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetDataValuesHandler implements CommandHandler {

    private final CliContext ctx;

    public GetDataValuesHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "get-data-values"; }
    public String getDescription() { return "读数据值"; }
    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/MMXU1.Volts"),
            new Param("fc", "功能约束 (留空=不限制)", "", FcInfo.enumChoices())
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String refs = values.get("refs");
        String fc = values.get("fc");

        CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST);
        for (String ref : refs.split(",")) {
            CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry().reference(ref.trim());
            if (!fc.isEmpty()) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        CmsGetDataValues resp = (CmsGetDataValues) response.getAsdu();
        System.out.println("  Data values (" + resp.value.size() + " entries):");
        for (int i = 0; i < resp.value.size(); i++) {
            String raw = resp.value.get(i).toString();
            if (raw.contains("CmsServiceError")) {
                System.out.println("    [" + i + "] " + CmsColor.red("Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError ")));
            } else {
                System.out.println("    [" + i + "] " + raw);
            }
        }
        if (resp.moreFollows.get()) {
            System.out.println(CmsColor.gray("  (more data available)"));
        }
    }
}
