package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class AbortHandler implements CommandHandler {

    private final CliContext ctx;

    public AbortHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "abort"; }
    public String getDescription() { return "异常中止关联"; }
    public List<Param> getParams() {
        return List.of(new Param("reason", "中止原因", "4", List.of(
            new Param.EnumChoice("0", "正常中止"),
            new Param.EnumChoice("1", "内存不足"),
            new Param.EnumChoice("2", "通信中断"),
            new Param.EnumChoice("3", "未知错误"),
            new Param.EnumChoice("4", "其他")
        )));
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println(CmsColor.gray("  Not connected."));
            return;
        }
        int reason = Integer.parseInt(values.get("reason"));
        CmsAbort reqAsdu = new CmsAbort(MessageType.REQUEST).reason(reason);
        System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
        client.abort(reason);
        System.out.println(CmsColor.green("  Abort sent"));
    }
}
