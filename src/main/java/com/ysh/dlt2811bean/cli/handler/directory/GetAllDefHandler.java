package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllDefHandler extends AbstractServiceHandler {

    private String target;

    public GetAllDefHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_DEFINITION); }

    @Override
    protected List<Param> setParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            Param.fc("功能约束")
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        target = stringVal("target");
        String fc = stringVal("fc");
        CmsGetAllDataDefinition asdu = new CmsGetAllDataDefinition(MessageType.REQUEST);
        if (target.contains("/")) asdu.lnReference(target);
        else asdu.ldName(target);
        if (!fc.isEmpty()) asdu.fc(fc);
        response = sendAndVerify(client, asdu);
    }

    public void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
        List<CmsDataDefinitionEntry> entries = asdu.data().toList();
        CliPrinter.printList("Data Definitions (" + entries.size() + " entries)", entries, entry -> {
            String cdc = entry.cdcType().get();
            String ref = entry.reference().get();
            if (cdc == null) return ref;
            CdcInfo cdcInfo = CdcInfo.byName(cdc);
            String cdcDisplay = "  cdc=" + cdc + (cdcInfo != null ? CmsColor.gray(" (" + cdcInfo.getChineseName() + ")") : "");
            return ref + cdcDisplay;
        });
        if (target.contains("/")) {
            entries.forEach(entry -> ctx.addDataDefinition(target, entry));
        }
    }
}
