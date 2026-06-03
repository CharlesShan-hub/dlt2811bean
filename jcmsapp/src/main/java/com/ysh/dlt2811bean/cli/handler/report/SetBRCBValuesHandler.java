package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.report.CmsSetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetBRCBValuesHandler extends AbstractServiceHandler {

    public SetBRCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_BRCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "BRCB 引用", "C1/LLN0.PosReport").type(Param.Type.BRCB_REF),
            new Param("rptEna", "启用报告 (true/false)", ""),
            new Param("rptID", "报告 ID", ""),
            new Param("datSet", "数据集引用", ""),
            new Param("optFlds", "选项域 (10-bit 数值)", ""),
            new Param("bufTm", "缓存时间/ms", ""),
            new Param("trgOps", "触发条件 (6-bit 数值)", ""),
            new Param("intgPd", "完整性周期/ms", ""),
            new Param("gi", "触发 GI (true/false)", ""),
            new Param("purgeBuf", "清除缓存 (true/false)", ""),
            new Param("entryID", "条目 ID (八进制字符串)", ""),
            new Param("resvTms", "保留时间/s", "")
        );
    }

    protected void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = values.get("ref");
        if (ref == null || ref.isEmpty()) return;
        CmsSetBRCBValuesEntry entry = new CmsSetBRCBValuesEntry();
        entry.reference.set(ref);
        if (!values.getOrDefault("rptEna", "").isEmpty())
            entry.rptEna.set(Boolean.parseBoolean(values.get("rptEna")));
        if (!values.getOrDefault("rptID", "").isEmpty())
            entry.rptID.set(values.get("rptID"));
        if (!values.getOrDefault("datSet", "").isEmpty())
            entry.datSet.set(values.get("datSet"));
        if (!values.getOrDefault("optFlds", "").isEmpty())
            entry.optFlds.set(Long.parseLong(values.get("optFlds")));
        if (!values.getOrDefault("bufTm", "").isEmpty())
            entry.bufTm.set(Long.parseLong(values.get("bufTm")));
        if (!values.getOrDefault("trgOps", "").isEmpty())
            entry.trgOps.set(Long.parseLong(values.get("trgOps")));
        if (!values.getOrDefault("intgPd", "").isEmpty())
            entry.intgPd.set(Long.parseLong(values.get("intgPd")));
        if (!values.getOrDefault("gi", "").isEmpty())
            entry.gi.set(Boolean.parseBoolean(values.get("gi")));
        if (!values.getOrDefault("purgeBuf", "").isEmpty())
            entry.purgeBuf.set(Boolean.parseBoolean(values.get("purgeBuf")));
        if (!values.getOrDefault("entryID", "").isEmpty())
            entry.entryID.set(values.get("entryID").getBytes());
        if (!values.getOrDefault("resvTms", "").isEmpty())
            entry.resvTms.set(Integer.parseInt(values.get("resvTms")));

        CmsSetBRCBValues asdu = new CmsSetBRCBValues(MessageType.REQUEST);
        asdu.addBrcb(entry);

        response = client.send(asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  BRCB values set successfully"));
            String ref = values.get("ref");
            if (!values.getOrDefault("rptEna", "").isEmpty())
                ctx.updateBrcbAttribute(ref, "rptEna", values.get("rptEna"));
            if (!values.getOrDefault("rptID", "").isEmpty())
                ctx.updateBrcbAttribute(ref, "rptID", values.get("rptID"));
            if (!values.getOrDefault("datSet", "").isEmpty())
                ctx.updateBrcbAttribute(ref, "datSet", values.get("datSet"));
        } else {
            System.out.println(CmsColor.red("  Server error: " + response.getAsdu()));
        }
    }
}