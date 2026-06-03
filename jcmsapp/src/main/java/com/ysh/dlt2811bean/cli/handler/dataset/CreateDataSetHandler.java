package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class CreateDataSetHandler extends AbstractServiceHandler {

    public CreateDataSetHandler(CliContext ctx) { super(ctx, ServiceInfo.CREATE_DATA_SET); }

    protected List<Param> setParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF),
            new Param("ref", "成员引用", "C1/MMXU1.Volts").type(Param.Type.REFERENCE),
            Param.fc("功能约束"),
            new Param("referenceAfter", "追加位置 (可选, 为空则新建)", "", false)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        String ref = stringVal("ref");
        String fc = stringVal("fc");
        String referenceAfter = stringVal("referenceAfter");

        CmsCreateDataSet asdu = new CmsCreateDataSet(MessageType.REQUEST)
                .datasetReference(dsRef)
                .addMemberData(ref, fc);
        if (!referenceAfter.isEmpty()) {
            asdu.referenceAfter(referenceAfter);
        }
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx >= 0) {
            String ldName = dsRef.substring(0, slashIdx);
            String rest = dsRef.substring(slashIdx + 1);
            int dotIdx = rest.indexOf('.');
            String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;
            String lnName = dotIdx >= 0 ? rest.substring(0, dotIdx) : rest;
            Map<String, Object> dataSetMap = ctx.addDataSetGroup(ldName, lnName);
            if (!dataSetMap.containsKey(dsName)) {
                dataSetMap.put(dsName, null);
            }
        }
        CliPrinter.success("Dataset created successfully");
    }
}
