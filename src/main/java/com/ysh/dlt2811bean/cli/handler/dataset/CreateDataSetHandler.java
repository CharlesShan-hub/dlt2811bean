package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class CreateDataSetHandler extends AbstractServiceHandler {

    public CreateDataSetHandler(CliContext ctx) { super(ctx, ServiceInfo.CREATE_DATA_SET); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF),
            new Param("ref", "成员引用", "C1/MMXU1.Volts").type(Param.Type.REFERENCE),
            Param.fc("功能约束"),
            new Param("referenceAfter", "追加位置 (可选, 为空则新建)", "", false)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        String ref = values.get("ref");
        String fc = values.get("fc");
        String referenceAfter = values.get("referenceAfter");

        CmsApdu response;
        if (referenceAfter != null && !referenceAfter.isEmpty()) {
            response = client.createDataSet(dsRef, referenceAfter, ref, fc);
        } else {
            response = client.createDataSet(dsRef, ref, fc);
        }

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            // 更新本地 cache
            int slashIdx = dsRef.indexOf('/');
            if (slashIdx >= 0) {
                String ldName = dsRef.substring(0, slashIdx);
                String rest = dsRef.substring(slashIdx + 1);
                int dotIdx = rest.indexOf('.');
                String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;
                String lnName = dotIdx >= 0 ? rest.substring(0, dotIdx) : rest;
                Map<String, Object> dataSetMap = ctx.lnEntry(ldName, lnName).get("DATA_SET");
                if (dataSetMap == null) {
                    dataSetMap = new java.util.LinkedHashMap<>();
                    ctx.lnEntry(ldName, lnName).put("DATA_SET", dataSetMap);
                }
                if (!dataSetMap.containsKey(dsName)) {
                    dataSetMap.put(dsName, null);
                }
            }
            System.out.println(CmsColor.green("  Dataset created successfully"));
        }
    }
}
