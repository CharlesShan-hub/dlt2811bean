package com.ysh.dlt2811bean.cli.handler.association;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class AbortHandler extends AbstractServiceHandler {

    public AbortHandler(CliContext ctx) { super(ctx, ServiceInfo.ABORT); }
    
    protected List<Param> setParams() {
        return List.of(new Param("reason", "中止原因", "4", List.of(
            new Param.EnumChoice("0", "其他"),
            new Param.EnumChoice("1", "无法识别的服务"),
            new Param.EnumChoice("2", "无效的请求ID"),
            new Param.EnumChoice("3", "无效的参数"),
            new Param.EnumChoice("4", "无效的结果"),
            new Param.EnumChoice("5", "超出最大未完成服务数")
        )));
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        ctx.getAutoTestHeartbeat().stop();
        client.abort(val("reason"));
        CliPrinter.success("Abort sent");
        ctx.getCachedHierarchy().clear();
    }
}
