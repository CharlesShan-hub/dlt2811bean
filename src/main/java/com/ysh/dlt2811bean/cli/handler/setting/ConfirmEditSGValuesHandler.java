package com.ysh.dlt2811bean.cli.handler.setting;

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

public class ConfirmEditSGValuesHandler extends AbstractServiceHandler {

    public ConfirmEditSGValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.CONFIRM_EDIT_SG_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("sgRef", "定值组控制块引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String sgRef = values.get("sgRef");
        CmsApdu response = client.confirmEditSGValues(sgRef);

        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Edit SG values confirmed"));
        }
    }
}
