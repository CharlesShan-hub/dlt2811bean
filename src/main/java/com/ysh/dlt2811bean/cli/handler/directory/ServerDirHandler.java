package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerDirHandler extends AbstractServiceHandler {

    public ServerDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_SERVER_DIRECTORY); }

    public List<Param> getParams() {
        return List.of(
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.LD_NAME)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsGetServerDirectory reqAsdu = new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
        String after = values.get("referenceAfter");
        if (after != null && !after.isEmpty()) {
            reqAsdu.referenceAfter(after);
        }
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetServerDirectory resAsdu = (CmsGetServerDirectory) response.getAsdu();
        List<String> refs = resAsdu.reference().toList().stream().map(r -> r.get()).collect(Collectors.toList());
        CliPrinter.printList("Logical devices", refs, item -> item);
        for (String ref : refs) {
            ctx.ldEntry(ref);
        }
    }
}
