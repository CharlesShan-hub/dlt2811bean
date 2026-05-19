package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.cli.util.CacheTypeResolver;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetDataValuesHandler extends AbstractServiceHandler {

    public SetDataValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/LPHD1.Proxy.stVal").type(Param.Type.DA_TARGET),
            new Param("value", "要设置的值", "true"),
            Param.fc()
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String refs = stringVal("refs");
        String val = stringVal("value");
        String fc = stringVal("fc");

        String[] refArr = refs.split(",");
        String[] valArr = val.split(",");

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        for (int i = 0; i < refArr.length; i++) {
            String ref = refArr[i].trim();
            String v = i < valArr.length ? valArr[i].trim() : valArr[valArr.length - 1].trim();

            CmsType<?> typedValue = CacheTypeResolver.resolveFromCache(ctx, ref, v);
            if (typedValue == null) {
                typedValue = new CmsVisibleString(v).max(255);
            }

            CmsSetDataValuesEntry entry = new CmsSetDataValuesEntry()
                .reference(ref)
                .value(typedValue);
            if (!fc.isEmpty()) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        response = client.send(asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String refs = stringVal("refs");
        String val = stringVal("value");
        String[] refArr = refs.split(",");
        String[] valArr = val.split(",");

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            CliPrinter.success("All data values set successfully");
            for (int i = 0; i < refArr.length; i++) {
                ctx.addDataObjectValue(refArr[i].trim(),
                        i < valArr.length ? valArr[i].trim() : valArr[valArr.length - 1].trim());
            }
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
}
