package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsOperate;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class OperateHandler extends AbstractServiceHandler {

    public OperateHandler(CliContext ctx) { super(ctx, ServiceInfo.OPERATE); }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
            new Param("value", "控制值", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("reference");
        boolean val = Boolean.parseBoolean(values.get("value"));
        CmsOperate asdu = new CmsOperate(MessageType.REQUEST).reference(ref)
                .ctlVal(new CmsBoolean(val)).ctlNum(1).test(false);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Operate failed");
            return;
        }
        System.out.println("  Operated: " + ref + " with value " + val);
    }
}
