package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllDefHandler extends AbstractServiceHandler {

    private String target;

    public GetAllDefHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_DEFINITION); }

    @Override
    protected List<Param> setParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            Param.fc("功能约束")
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        target = stringVal("target");
        String fc = stringVal("fc");
        CmsGetAllDataDefinition reqAsdu = new CmsGetAllDataDefinition(MessageType.REQUEST);
        if (target.contains("/")) reqAsdu.lnReference(target);
        else reqAsdu.ldName(target);
        if (!fc.isEmpty()) reqAsdu.fc(fc);
        response = client.send(reqAsdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            CmsGetAllDataDefinition errAsdu = (CmsGetAllDataDefinition) response.getAsdu();
            CliPrinter.error("GetAllDataDefinition failed: " + errAsdu.serviceError);
        }
    }

    public void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        List<CmsDataDefinitionEntry> entries = asdu.data().toList();
        CliPrinter.printList("Data Definitions (" + entries.size() + " entries)", entries, entry -> {
            String cdc = entry.cdcType().get();
            String ref = entry.reference().get();
            if (cdc == null) return ref;
            CdcInfo cdcInfo = CdcInfo.byName(cdc);
            String cdcDisplay = "  cdc=" + cdc + (cdcInfo != null ? CmsColor.gray(" (" + cdcInfo.getChineseName() + ")") : "");
            return ref + cdcDisplay;
        });
        if (target.contains("/")) {
            String[] parts = target.split("/", 2);
            String ldName = parts[0];
            String lnName = parts[1];
            Map<String, Object> dataObjectGroup = ctx.addDataObjectGroup(ldName, lnName);
            for (CmsDataDefinitionEntry entry : entries) {
                String doName = entry.reference().get();
                CmsDataDefinition def = entry.definition();
                if (def != null && def.getStructureEntries() != null) {
                    for (CmsDataDefinition.StructureEntry se : def.getStructureEntries()) {
                        String fc = se.fc.get();
                        String daName = se.name.get();
                        String typeName = se.type != null ? se.type.typeName() : "?";
                        ctx.addDataObjectType(dataObjectGroup, doName, fc, daName, typeName);
                    }
                }
            }
        }
    }

}
