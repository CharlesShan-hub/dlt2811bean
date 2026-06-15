package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetValues;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetDataSetValuesHandler extends AbstractServiceHandler {

    public GetDataSetValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_SET_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        CmsGetDataSetValues asdu = new CmsGetDataSetValues(MessageType.REQUEST).datasetReference(dsRef);
        response = sendAndVerify(client, asdu);
    }

    @SuppressWarnings("unchecked")
    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        CmsGetDataSetValues resp = (CmsGetDataSetValues) response.getAsdu();
        List<CmsData<?>> dataList = resp.value.toList();
        CliPrinter.printList("Dataset values (" + dataList.size() + " entries)", dataList, item -> {
            String raw = item.toString();
            if (raw.contains("CmsServiceError")) {
                return "Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError ");
            }
            return raw;
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());

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
                            for (int i = 0; i < dataList.size(); i++) {
                                Map<String, Object> memberMap = (Map<String, Object>) orderedMembers.get(String.valueOf(i));
                                if (memberMap != null) {
                                    Object doRef = memberMap.get("DO");
                                    if (doRef instanceof Map) {
                                        String val = dataList.get(i).toString();
                                        ((Map<String, Object>) doRef).put("value", val);
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
