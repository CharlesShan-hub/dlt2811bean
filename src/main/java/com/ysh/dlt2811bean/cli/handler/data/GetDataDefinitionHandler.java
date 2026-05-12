package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDataDefinitionHandler extends AbstractServiceHandler {

    public GetDataDefinitionHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_DEFINITION); }
    
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
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < resp.definition.size(); i++) {
            var entry = resp.definition.get(i);
            String cdc = entry.cdcType().get();
            CmsDataDefinition def = entry.definition();
            
            StringBuilder sb = new StringBuilder();
            
            // Format: [0] FLOAT32  cdc=MC (测量值)
            String defStr = formatDefinition(def, "");
            if (!defStr.isEmpty()) {
                sb.append(" ").append(CmsColor.green(defStr));
            }
            
            if (cdc != null && !cdc.isEmpty()) {
                CdcInfo cdcInfo = CdcInfo.byName(cdc);
                sb.append("  cdc=").append(cdc);
                if (cdcInfo != null) {
                    sb.append(CmsColor.gray(" (")).append(cdcInfo.getChineseName()).append(")");
                }
            }
            
            lines.add(sb.toString());
        }
        printList("Data definitions (" + lines.size() + " entries)", lines, item -> item);
        printMoreFollows(resp.moreFollows.get());
    }
    
    private String formatDefinition(CmsDataDefinition def, String indent) {
        if (def == null) return "";
        int tag = def.get();
        switch (tag) {
            case CmsDataDefinition.ERROR:
                return indent + CmsColor.red("ERROR");
            case CmsDataDefinition.BOOLEAN:
                return indent + "BOOLEAN";
            case CmsDataDefinition.INT8:
                return indent + "INT8";
            case CmsDataDefinition.INT16:
                return indent + "INT16";
            case CmsDataDefinition.INT32:
                return indent + "INT32";
            case CmsDataDefinition.INT64:
                return indent + "INT64";
            case CmsDataDefinition.INT8U:
                return indent + "INT8U";
            case CmsDataDefinition.INT16U:
                return indent + "INT16U";
            case CmsDataDefinition.INT32U:
                return indent + "INT32U";
            case CmsDataDefinition.INT64U:
                return indent + "INT64U";
            case CmsDataDefinition.FLOAT32:
                return indent + "FLOAT32";
            case CmsDataDefinition.FLOAT64:
                return indent + "FLOAT64";
            case CmsDataDefinition.VISIBLE_STRING:
                return indent + "VISIBLE_STRING";
            case CmsDataDefinition.UNICODE_STRING:
                return indent + "UNICODE_STRING";
            case CmsDataDefinition.OCTET_STRING:
                return indent + "OCTET_STRING";
            case CmsDataDefinition.QUALITY:
                return indent + "QUALITY";
            case CmsDataDefinition.DBPOS:
                return indent + "DBPOS";
            case CmsDataDefinition.TCMD:
                return indent + "TCMD";
            case CmsDataDefinition.CHECK:
                return indent + "CHECK";
            case CmsDataDefinition.STRUCTURE: {
                java.util.List<CmsDataDefinition.StructureEntry> entries = def.getStructureEntries();
                if (entries == null || entries.isEmpty()) {
                    return indent + "STRUCTURE (empty)";
                }
                StringBuilder sb = new StringBuilder();
                sb.append(indent).append("STRUCTURE:");
                for (CmsDataDefinition.StructureEntry se : entries) {
                    sb.append("\n").append(indent).append("  ├─ ").append(se.name.get());
                    if (se.fc != null) {
                        sb.append(" [").append(se.fc.get()).append("]");
                    }
                    sb.append(" : ").append(formatDefinition(se.type, indent + "  "));
                }
                return sb.toString();
            }
            default:
                return indent + "UNKNOWN(" + tag + ")";
        }
    }
}
