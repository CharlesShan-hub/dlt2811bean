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
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        CmsApdu response = client.deleteDataSet(dsRef);

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Dataset deleted successfully"));
        }
    }
}
