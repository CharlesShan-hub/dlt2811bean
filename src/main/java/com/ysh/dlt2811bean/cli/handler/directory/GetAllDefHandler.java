package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetAllDefHandler extends AbstractServiceHandler {

    public GetAllDefHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_DEFINITION); }
    
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            Param.fc("功能约束")
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
        List<CmsDataDefinitionEntry> entries = asdu.data().toList();
        CliPrinter.printList("Data definitions (" + entries.size() + " entries)", entries, entry -> {
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
            java.util.Map<String, Object> existing = ctx.lnEntry(ldName, lnName).get("DATA_OBJECT");
            if (existing == null) {
                java.util.Map<String, Object> doMap = new java.util.LinkedHashMap<>();
                for (CmsDataDefinitionEntry entry : entries) {
                    String doName = entry.reference().get();
                    CmsDataDefinition def = entry.definition();
                    java.util.Map<String, Object> daMap = new java.util.LinkedHashMap<>();
                    if (def != null && def.getStructureEntries() != null) {
                        for (CmsDataDefinition.StructureEntry se : def.getStructureEntries()) {
                            Map<String, Object> daValue = new LinkedHashMap<>();
                            daValue.put("type", choiceIndexToTypeName(se.type.getChoiceIndex()));
                            daValue.put("value", null);
                            daMap.put(se.name.get(), daValue);
                        }
                    }
                    doMap.put(doName, daMap);
                }
                ctx.putAcdEntry(ldName, lnName, "DATA_OBJECT", doMap);
            }
        }
    }

    private static String choiceIndexToTypeName(int choiceIndex) {
        return switch (choiceIndex) {
            case CmsDataDefinition.BOOLEAN -> "BOOLEAN";
            case CmsDataDefinition.INT8 -> "INT8";
            case CmsDataDefinition.INT16 -> "INT16";
            case CmsDataDefinition.INT32 -> "INT32";
            case CmsDataDefinition.INT64 -> "INT64";
            case CmsDataDefinition.INT8U -> "INT8U";
            case CmsDataDefinition.INT16U -> "INT16U";
            case CmsDataDefinition.INT32U -> "INT32U";
            case CmsDataDefinition.INT64U -> "INT64U";
            case CmsDataDefinition.FLOAT32 -> "FLOAT32";
            case CmsDataDefinition.FLOAT64 -> "FLOAT64";
            case CmsDataDefinition.BIT_STRING -> "BIT STRING";
            case CmsDataDefinition.OCTET_STRING -> "OCTET STRING";
            case CmsDataDefinition.VISIBLE_STRING -> "VISIBLE STRING";
            case CmsDataDefinition.UNICODE_STRING -> "UNICODE STRING";
            case CmsDataDefinition.UTC_TIME -> "Timestamp";
            case CmsDataDefinition.BINARY_TIME -> "BinaryTime";
            case CmsDataDefinition.QUALITY -> "Quality";
            case CmsDataDefinition.DBPOS -> "Dbpos";
            case CmsDataDefinition.TCMD -> "Tcmd";
            case CmsDataDefinition.CHECK -> "Check";
            case CmsDataDefinition.STRUCTURE -> "Struct";
            case CmsDataDefinition.ARRAY -> "Array";
            default -> "Unknown(" + choiceIndex + ")";
        };
    }
}
