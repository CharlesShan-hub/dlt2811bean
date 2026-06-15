package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetDataDirectoryHandler extends AbstractServiceHandler {

    public GetDataDirectoryHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_DIRECTORY); }

    protected List<Param> setParams() {
        return List.of(
            new Param("ref", "数据引用 [string]", "C1/LPHD1.Proxy").type(Param.Type.DA_REF),
            new Param("after", "起始引用 (留空=从头) [string]", "").type(Param.Type.DA_NAME_NOT_NULL)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = stringVal("ref");
        String after = stringVal("after");

        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST).dataReference(ref);
        if (!after.isEmpty()) asdu.referenceAfter(after);

        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CmsGetDataDirectory resp = (CmsGetDataDirectory) response.getAsdu();
        List<CmsGetDataDirectoryEntry> entries = resp.dataAttribute.toList();
        CliPrinter.printList("Directory (" + entries.size() + " entries)",
                entries, entry -> {
                    String fcStr = entry.fc.get();
                    if (fcStr != null && !fcStr.isEmpty()) 
                        return entry.reference.get() + "  [" + fcStr + "]";
                    return entry.reference.get();
                });
        if (resp.moreFollows.get()) {
            CliPrinter.info("more data available, use after=<last> to continue");
        }
    }
}
