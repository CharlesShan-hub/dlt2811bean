package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllDefHandler extends AbstractServiceHandler {

    public GetAllDefHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_DEFINITION); }
    public String getDescription() { return "读所有数据定义"; }
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1"),
            new Param("fc", "功能约束 (留空=全部)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String target = values.get("target");
        String fc = values.get("fc");
        CmsGetAllDataDefinition reqAsdu = new CmsGetAllDataDefinition(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        if (!printIfEmpty(asdu.data().isEmpty())) {
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
