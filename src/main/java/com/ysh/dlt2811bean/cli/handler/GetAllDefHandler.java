package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllDefHandler extends AbstractServiceHandler {

    public GetAllDefHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "get-all-def"; }
    public String getDescription() { return "读所有数据定义"; }
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1"),
            new Param("fc", "功能约束 (留空=全部)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }
        String target = values.get("target");
        String fc = values.get("fc");
        CmsGetAllDataDefinition reqAsdu = new CmsGetAllDataDefinition(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
        CmsApdu response = ctx.sendAndPrint(client, reqAsdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed"); return;
        }
        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        if (asdu.data().isEmpty()) {
            System.out.println(CmsColor.gray("  无数据"));
        } else {
            System.out.println("  Data definitions (" + asdu.data().size() + " entries):");
            for (int i = 0; i < asdu.data().size(); i++) {
                CmsDataDefinitionEntry entry = asdu.data().get(i);
                String cdc = entry.cdcType().get();
                String cdcDisplay = "";
                if (cdc != null) {
                    CdcInfo cdcInfo = CdcInfo.byName(cdc);
                    cdcDisplay = "  cdc=" + cdc + (cdcInfo != null ? CmsColor.gray(" (" + cdcInfo.getChineseName() + ")") : "");
                }
                System.out.println("    [" + i + "] " + entry.reference().get() + cdcDisplay);
            }
        }
    }
}
