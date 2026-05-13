package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetDataValuesHandler extends AbstractServiceHandler {

    public SetDataValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/LPHD1.Proxy.stVal").type(Param.Type.DA_TARGET),
            new Param("value", "要设置的值", "true"),
            Param.fc()
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String refs = values.get("refs");
        String val = values.get("value");
        String fc = values.get("fc");

        String[] refArr = refs.split(",");
        String[] valArr = val.split(",");

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        for (int i = 0; i < refArr.length; i++) {
            String ref = refArr[i].trim();
            String v = i < valArr.length ? valArr[i].trim() : valArr[valArr.length - 1].trim();

            CmsType<?> typedValue = SclTypeResolver.resolveTypedValue(config, ref, v);

            CmsSetDataValuesEntry entry = new CmsSetDataValuesEntry()
                .reference(ref)
                .value(typedValue);
            if (!fc.isEmpty()) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  All data values set successfully"));
            updateCache(refArr, valArr);
        } else if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            CmsSetDataValues resp = (CmsSetDataValues) response.getAsdu();
            List<String> failures = new ArrayList<>();
            for (int i = 0; i < resp.result.size() && i < refArr.length; i++) {
                int errorCode = resp.result.get(i).get();
                if (errorCode != CmsServiceError.NO_ERROR) {
                    failures.add(refArr[i].trim() + " -> error " + errorCode);
                }
            }
            CliPrinter.printList("Some or all values failed", failures, item -> CmsColor.red(item));
        }
    }

    private void updateCache(String[] refArr, String[] valArr) {
        for (int i = 0; i < refArr.length; i++) {
            String ref = refArr[i].trim();
            if (!ref.contains("/") || !ref.contains(".")) continue;
            String[] parts = ref.split("\\.");
            if (parts.length < 2) continue;
            String[] ldLn = parts[0].split("/", 2);
            if (ldLn.length < 2) continue;
            String ld = ldLn[0], ln = ldLn[1];
            String doName = parts[1];
            java.util.Map<String, Object> das = ctx.lnEntry(ld, ln).get("DATA_OBJECT");
            if (das == null) continue;
            String v = i < valArr.length ? valArr[i].trim() : valArr[valArr.length - 1].trim();
            if (parts.length >= 3) {
                String daName = parts[2];
                java.util.Map<String, Object> doMap = (java.util.Map<String, Object>) das.get(doName);
                if (doMap != null) {
                    doMap.put(daName, v);
                }
            } else {
                java.util.Map<String, Object> doMap = (java.util.Map<String, Object>) das.get(doName);
                if (doMap == null) {
                    doMap = new java.util.LinkedHashMap<>();
                    das.put(doName, doMap);
                }
                doMap.put("value", v);
            }
        }
    }
}
