package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogByTime;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class QueryLogByTimeHandler extends AbstractServiceHandler {

    public QueryLogByTimeHandler(CliContext ctx) { super(ctx, ServiceInfo.QUERY_LOG_BY_TIME); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "LCB 引用", "C1/LLN0.Log"),
            new Param("start", "起始时间 (二进制时间)", ""),
            new Param("stop", "结束时间 (二进制时间)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.queryLogByTime(ref);
        CmsQueryLogByTime resp = (CmsQueryLogByTime) response.getAsdu();
        List<CmsLogEntry> entries = resp.logEntry.toList();
        CliPrinter.printList("Log entries (" + entries.size() + " entries)", entries, Object::toString);
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
