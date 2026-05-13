package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetDataSetValuesHandler extends AbstractServiceHandler {

    public SetDataSetValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_SET_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");

        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference(dsRef);

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Dataset values set successfully"));
        }
    }
}
