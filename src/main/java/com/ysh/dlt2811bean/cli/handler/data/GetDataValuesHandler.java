package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetDataValuesHandler extends AbstractServiceHandler {

    public GetDataValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/MMXU1.Volts").type(Param.Type.DA_REF),
            Param.fc()
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

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

        CmsApdu response = sendAndVerify(client, asdu);

        CmsGetDataValues resp = (CmsGetDataValues) response.getAsdu();
        List<CmsData<?>> dataList = resp.value.toList();
        CliPrinter.printList("Data values (" + dataList.size() + " entries)", dataList, item -> {
            String raw = item.toString();
            if (raw.contains("CmsServiceError")) {
                return CmsColor.red("Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError "));
            }
            return raw;
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());

        String[] refArr = refs.split(",");
        for (int i = 0; i < refArr.length && i < dataList.size(); i++) {
            String ref = refArr[i].trim();
            CmsData<?> data = dataList.get(i);
            updateCache(ref, data);
        }
    }

    private void updateCache(String ref, CmsData<?> data) {
        if (!ref.contains("/") || !ref.contains(".")) return;
        String[] parts = ref.split("\\.");
        if (parts.length < 2) return;
        String[] ldLn = parts[0].split("/", 2);
        if (ldLn.length < 2) return;
        String ld = ldLn[0], ln = ldLn[1];
        String doName = parts[1];
        java.util.Map<String, Object> das = ctx.lnEntry(ld, ln).get("DATA_OBJECT");
        if (das == null) return;
        if (parts.length >= 3) {
            String daName = parts[2];
            java.util.Map<String, Object> doMap = (java.util.Map<String, Object>) das.get(doName);
            if (doMap != null) {
                doMap.put(daName, data.toString());
            }
        } else {
            java.util.Map<String, Object> doMap = (java.util.Map<String, Object>) das.get(doName);
            if (doMap == null) {
                doMap = new java.util.LinkedHashMap<>();
                das.put(doName, doMap);
            }
            doMap.put("value", data.toString());
        }
    }
}
