package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetDataSetDirectoryHandler extends AbstractServiceHandler {

    public GetDataSetDirectoryHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_SET_DIRECTORY); }

    protected List<Param> setParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        CmsGetDataSetDirectory asdu = new CmsGetDataSetDirectory(MessageType.REQUEST).datasetReference(dsRef);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String dsRef = stringVal("dsRef");
        CmsGetDataSetDirectory resp = (CmsGetDataSetDirectory) response.getAsdu();
        List<CmsCreateDataSetEntry> entries = resp.memberData.toList();
        CliPrinter.printList("Dataset members (" + entries.size() + " entries)", entries,
                item -> item.reference.get() + (item.fc.get().isEmpty() ? "" : "  fc=" + item.fc.get()));
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
