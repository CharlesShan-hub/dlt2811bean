package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.type.CmsScalar;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import java.util.List;
import java.util.Map;

public class GetAllValuesHandler extends AbstractServiceHandler {

    public GetAllValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_VALUES); }
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            Param.fc("功能约束"),
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String target = values.get("target");
        String fc = values.get("fc");
        String after = values.get("referenceAfter");
        CmsGetAllDataValues reqAsdu = new CmsGetAllDataValues(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
        if (!after.isEmpty()) reqAsdu.referenceAfter(after);
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        List<CmsDataEntry> entries = asdu.data().toList();
        CliPrinter.printList("Data values (" + entries.size() + " entries)", entries,
                item -> {
                    String ref = item.reference().get();
                    CmsData<?> data = item.value();
                    String valueStr = formatCmsDataValue(data);
                    return ref + " = " + valueStr;
                });
    }

    /**
     * Formats a CmsData value for display, showing the type name and value.
     * e.g. "FLOAT32(100.0)" or "BOOLEAN(true)"
     */
    private String formatCmsDataValue(CmsData<?> data) {
        CmsType<?> inner = data.getInnerValue();
        if (inner == null) return CmsColor.gray("null");

        // Extract type name from class (e.g. "CmsInt32" -> "INT32")
        String simpleName = inner.getClass().getSimpleName();
        String typeName = simpleName.startsWith("Cms") ? simpleName.substring(3).toUpperCase() : simpleName.toUpperCase();

        // Get the value string
        String valStr;
        if (inner instanceof CmsScalar) {
            Object val = ((CmsScalar<?, ?>) inner).get();
            valStr = val != null ? val.toString() : "null";
        } else {
            valStr = inner.toString();
        }

        return CmsColor.green(typeName) + "(" + valStr + ")";
    }
}
