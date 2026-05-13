package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogAfter;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class QueryLogAfterHandler extends AbstractServiceHandler {

    public QueryLogAfterHandler(CliContext ctx) { super(ctx, ServiceInfo.QUERY_LOG_AFTER); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "LCB 引用", "C1/LLN0.Log"),
            new Param("entry", "参考点 EntryID", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.queryLogAfter(ref, null);
        CmsQueryLogAfter resp = (CmsQueryLogAfter) response.getAsdu();
        List<CmsLogEntry> entries = resp.logEntry.toList();
        CliPrinter.printList("Log entries (" + entries.size() + " entries)", entries, Object::toString);
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
