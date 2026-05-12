package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelectWithValue;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import java.util.List;
import java.util.Map;

public class SelectWithValueHandler extends AbstractServiceHandler {

    public SelectWithValueHandler(CliContext ctx) { super(ctx, ServiceInfo.SELECT_WITH_VALUE); }
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
        CmsSelectWithValue asdu = new CmsSelectWithValue(MessageType.REQUEST).reference(ref)
                .ctlVal(ctlVal).ctlNum(0).test(false);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.red("  SelectWithValue failed"));
            return;
        }
        System.out.println(CmsColor.green("  Selected: " + ref + " with value " + val));
    }
}
