package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetDataSetValuesHandler extends AbstractServiceHandler {

    public SetDataSetValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_SET_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF),
            new Param("value", "要设置的值 (逗号分隔，按数据集成员顺序)", "true"),
            new Param("referenceAfter", "从指定成员之后开始设置", "").type(Param.Type.DS_REF)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        String val = stringVal("value");
        String after = stringVal("referenceAfter");

        if (val.isEmpty()) {
            throw new IllegalArgumentException("value is required");
        }

        String[] valArr = val.split(",");

        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference(dsRef);

        if (!after.isEmpty()) {
            asdu.referenceAfter(after);
        }

        for (String v : valArr) {
            asdu.addMemberValue(new com.ysh.dlt2811bean.datatypes.string.CmsUtf8String(v.trim()).max(255));
        }

        response = sendAndVerify(client, asdu);
    }

    @SuppressWarnings("unchecked")
    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CliPrinter.success("Dataset values set successfully");

        String dsRef = stringVal("dsRef");
        String val = stringVal("value");
        String[] valArr = val.split(",");

        int slashIdx = dsRef.indexOf('/');
        int dotIdx = dsRef.indexOf('.');
        if (slashIdx >= 0 && dotIdx > slashIdx) {
            String ldName = dsRef.substring(0, slashIdx);
            String lnName = dsRef.substring(slashIdx + 1, dotIdx);
            String dsName = dsRef.substring(dotIdx + 1);
            Map<String, Map<String, Map<String, Object>>> ldMap = ctx.getCachedHierarchy().get(ldName);
            if (ldMap != null) {
                Map<String, Map<String, Object>> lnMap = ldMap.get(lnName);
                if (lnMap != null) {
                    Map<String, Object> dataSetMap = lnMap.get("DATA_SET");
                    if (dataSetMap != null) {
                        Object dsObj = dataSetMap.get(dsName);
                        if (dsObj instanceof Map) {
                            Map<String, Object> orderedMembers = (Map<String, Object>) dsObj;
                            for (int i = 0; i < valArr.length; i++) {
                                Map<String, Object> memberMap = (Map<String, Object>) orderedMembers.get(String.valueOf(i));
                                if (memberMap != null) {
                                    Object doRef = memberMap.get("DO");
                                    if (doRef instanceof Map) {
                                        ((Map<String, Object>) doRef).put("value", valArr[i].trim());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
