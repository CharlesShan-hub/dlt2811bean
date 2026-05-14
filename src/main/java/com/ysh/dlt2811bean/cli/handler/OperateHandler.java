package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsOperate;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import java.util.List;
import java.util.Map;

public class OperateHandler extends AbstractServiceHandler {

    public OperateHandler(CliContext ctx) { super(ctx, ServiceInfo.OPERATE); }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "C1/CSWI1.Pos").type(Param.Type.REFERENCE),
            new Param("value", "控制值", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("reference");
        String val = values.get("value");
        CmsType<?> ctlVal = SclTypeResolver.parseControlValue(config, ref, val);
        CmsOperate asdu = new CmsOperate(MessageType.REQUEST).reference(ref)
                .ctlVal(ctlVal).ctlNum(1).test(false);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.red("  Operate failed"));
            return;
        }
        System.out.println(CmsColor.green("  Operated: " + ref + " with value " + val));
    }
}
