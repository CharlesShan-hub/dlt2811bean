package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import java.util.List;
import java.util.Map;

public class SelectHandler extends AbstractServiceHandler {

    public SelectHandler(CliContext ctx) { super(ctx, ServiceInfo.SELECT); }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "C1/CSWI1.Pos").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("reference");
        CmsSelect asdu = new CmsSelect(MessageType.REQUEST).reference(ref);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.red("  Select failed"));
            return;
        }
        System.out.println(CmsColor.green("  Selected: " + ref));
    }
}
