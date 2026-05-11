package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCancel;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class CancelHandler extends AbstractServiceHandler {

    public CancelHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "cancel"; }
    public String getDescription() { return "取消控制操作 (撤销 select/operate)"; }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
            new Param("value", "控制值", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String ref = values.get("reference");
        boolean val = Boolean.parseBoolean(values.get("value"));
        CmsCancel asdu = new CmsCancel(MessageType.REQUEST).reference(ref)
                .ctlVal(new CmsBoolean(val)).ctlNum(2).test(false);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Cancel failed");
            return;
        }
        System.out.println("  Cancelled: " + ref + " with value " + val);
    }
}
