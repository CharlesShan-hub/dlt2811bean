package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetURCBValuesHandler extends AbstractServiceHandler {

    public SetURCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_URCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "URCB 引用", "C1/LLN0.PosReport").type(Param.Type.URCB_REF),
            new Param("rptEna", "启用报告 (true/false)", "true"),
            new Param("rptID", "报告 ID (留空=不设置)", ""),
            new Param("datSet", "数据集引用 (留空=不设置)", ""),
            new Param("intgPd", "完整性周期/ms (留空=不设置)", ""),
            new Param("gi", "触发 GI (true/false, 留空=不设置)", ""),
            new Param("bufTm", "缓存时间/ms (留空=不设置)", ""),
            new Param("resv", "预留 (true/false, 留空=不设置)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsSetURCBValuesEntry entry = new CmsSetURCBValuesEntry();
        entry.reference.set(ref);
        if (!values.getOrDefault("rptEna", "").isEmpty())
            entry.rptEna.set(Boolean.parseBoolean(values.get("rptEna")));
        if (!values.getOrDefault("rptID", "").isEmpty())
            entry.rptID.set(values.get("rptID"));
        if (!values.getOrDefault("datSet", "").isEmpty())
            entry.datSet.set(values.get("datSet"));
        if (!values.getOrDefault("intgPd", "").isEmpty())
            entry.intgPd.set(Long.parseLong(values.get("intgPd")));
        if (!values.getOrDefault("gi", "").isEmpty())
            entry.gi.set(Boolean.parseBoolean(values.get("gi")));
        if (!values.getOrDefault("bufTm", "").isEmpty())
            entry.bufTm.set(Long.parseLong(values.get("bufTm")));
        if (!values.getOrDefault("resv", "").isEmpty())
            entry.resv.set(Boolean.parseBoolean(values.get("resv")));

        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST);
        asdu.addUrcb(entry);

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  URCB values set successfully"));
        } else {
            System.out.println(CmsColor.red("  Server error: " + response.getAsdu()));
        }
    }
}
