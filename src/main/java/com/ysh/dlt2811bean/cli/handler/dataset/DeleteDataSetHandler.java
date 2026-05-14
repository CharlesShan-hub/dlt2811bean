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

public class DeleteDataSetHandler extends AbstractServiceHandler {

    public DeleteDataSetHandler(CliContext ctx) { super(ctx, ServiceInfo.DELETE_DATA_SET); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        CmsApdu response = client.deleteDataSet(dsRef);

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            // 清理本地 cache 中的数据集
            int slashIdx = dsRef.indexOf('/');
            if (slashIdx >= 0) {
                String ldName = dsRef.substring(0, slashIdx);
                String rest = dsRef.substring(slashIdx + 1);
                int dotIdx = rest.indexOf('.');
                String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;
                java.util.Map<String, Map<String, Map<String, Object>>> ldMap = ctx.getCachedHierarchy().get(ldName);
                if (ldMap != null) {
                    for (java.util.Map<String, Map<String, Object>> lnMap : ldMap.values()) {
                        Map<String, Object> dataSetMap = lnMap.get("DATA_SET");
                        if (dataSetMap != null) {
                            dataSetMap.remove(dsName);
                        }
                    }
                }
            }
            System.out.println(CmsColor.green("  Dataset deleted successfully"));
        }
    }
}
