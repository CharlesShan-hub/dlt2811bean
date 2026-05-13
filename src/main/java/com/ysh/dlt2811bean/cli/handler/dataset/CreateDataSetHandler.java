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
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.REFERENCE),
            new Param("ref", "成员引用", "C1/MMXU1.Volts").type(Param.Type.REFERENCE),
            Param.fc("功能约束"),
            new Param("after", "追加位置 (可选, 为空则新建)", "", false)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        String ref = values.get("ref");
        String fc = values.get("fc");
        String after = values.get("after");

        CmsApdu response;
        if (after != null && !after.isEmpty()) {
            response = client.createDataSet(dsRef, after, ref, fc);
        } else {
            response = client.createDataSet(dsRef, ref, fc);
        }

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Dataset created successfully"));
        }
    }
}
