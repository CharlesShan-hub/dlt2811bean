package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetDataDefinitionHandler extends AbstractServiceHandler {

    public GetDataDefinitionHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "get-data-def"; }
    public String getDescription() { return "读数据定义"; }
    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/MMXU1.Volts"),
            Param.fc()
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String refs = values.get("refs");
        String fc = values.get("fc");

        CmsGetDataDefinition asdu = new CmsGetDataDefinition(MessageType.REQUEST);
        for (String ref : refs.split(",")) {
            CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry().reference(ref.trim());
            if (!fc.isEmpty() && !"XX".equals(fc)) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        CmsApdu response = sendAndVerify(client, asdu);

        CmsGetDataDefinition resp = (CmsGetDataDefinition) response.getAsdu();
        System.out.println("  Data definitions (" + resp.definition.size() + " entries):");
        for (int i = 0; i < resp.definition.size(); i++) {
            var entry = resp.definition.get(i);
            String cdc = entry.cdcType().get();
            StringBuilder sb = new StringBuilder();
            sb.append("    [").append(i).append("]");
            if (cdc != null && !cdc.isEmpty()) {
                CdcInfo cdcInfo = CdcInfo.byName(cdc);
                sb.append("  cdc=").append(cdc);
                if (cdcInfo != null) {
                    sb.append(CmsColor.gray(" (")).append(cdcInfo.getChineseName()).append(")");
                }
            }
            System.out.println(sb.toString());
        }
        if (resp.moreFollows.get()) {
            System.out.println(CmsColor.gray("  (more data available)"));
        }
    }
}
