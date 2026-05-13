package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetDataSetDirectoryHandler extends AbstractServiceHandler {

    public GetDataSetDirectoryHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_SET_DIRECTORY); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        CmsApdu response = client.getDataSetDirectory(dsRef);
        if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            System.out.println(CmsColor.red("  Server error: dataset '" + dsRef + "' not found or inappropriate"));
            return;
        }
        CmsGetDataSetDirectory resp = (CmsGetDataSetDirectory) response.getAsdu();
        List<CmsCreateDataSetEntry> entries = resp.memberData.toList();
        CliPrinter.printList("Dataset members (" + entries.size() + " entries)", entries,
                item -> item.reference.get() + (item.fc.get().isEmpty() ? "" : "  fc=" + item.fc.get()));
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
