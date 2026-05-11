package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetDataDirectoryHandler extends AbstractServiceHandler {

    public GetDataDirectoryHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "get-data-dir"; }
    public String getDescription() { return "读数据目录"; }
    public List<Param> getParams() {
        return List.of(
            new Param("ref", "数据引用", "C1/LPHD1.Proxy"),
            new Param("after", "起始引用 (留空=从头)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String ref = values.get("ref");
        String after = values.get("after");

        CmsGetDataDirectory asdu = new CmsGetDataDirectory(MessageType.REQUEST)
                .dataReference(ref);
        if (!after.isEmpty()) {
            asdu.referenceAfter(after);
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        CmsGetDataDirectory resp = (CmsGetDataDirectory) response.getAsdu();
        System.out.println("  Directory (" + resp.dataAttribute.size() + " entries):");
        for (int i = 0; i < resp.dataAttribute.size(); i++) {
            CmsGetDataDirectoryEntry entry = resp.dataAttribute.get(i);
            String fcStr = entry.fc.get();
            if (fcStr != null && !fcStr.isEmpty()) {
                System.out.println("    [" + i + "] " + CmsColor.bold(entry.reference.get()) + "  " + CmsColor.cyan("[" + fcStr + "]"));
            } else {
                System.out.println("    [" + i + "] " + CmsColor.bold(entry.reference.get()));
            }
        }
        if (resp.moreFollows.get()) {
            System.out.println(CmsColor.gray("  (more data available, use after=<last> to continue)"));
        }
    }
}
