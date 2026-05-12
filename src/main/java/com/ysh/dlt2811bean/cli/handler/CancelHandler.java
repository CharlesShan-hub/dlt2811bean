package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCancel;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import java.util.List;
import java.util.Map;

public class CancelHandler extends AbstractServiceHandler {

    public CancelHandler(CliContext ctx) { super(ctx, ServiceInfo.CANCEL); }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "C1/CSWI1.Pos"),
            new Param("value", "控制值", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("reference");
        String val = values.get("value");
        CmsType<?> ctlVal = parseControlValue(ref, val);
        CmsCancel asdu = new CmsCancel(MessageType.REQUEST).reference(ref)
                .ctlVal(ctlVal).ctlNum(2).test(false);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.red("  Cancel failed"));
            return;
        }
        System.out.println(CmsColor.green("  Cancelled: " + ref + " with value " + val));
    }
}
