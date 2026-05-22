package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllCbHandler extends AbstractServiceHandler {

    public GetAllCbHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_CB_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            new Param("type", "控制块类型", "URCB", List.of(
                new Param.EnumChoice("BRCB", "报告控制块（缓存）"),
                new Param.EnumChoice("URCB", "报告控制块（非缓存）"),
                new Param.EnumChoice("LCB", "日志控制块"),
                new Param.EnumChoice("GO_CB", "GOOSE 控制块"),
                new Param.EnumChoice("MSV_CB", "采样值控制块"),
                new Param.EnumChoice("SGCB", "定值组控制块")
            ))
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String target = stringVal("target");
        int acsiClass = parseAcsi(stringVal("type"));
        CmsGetAllCBValues reqAsdu = new CmsGetAllCBValues(MessageType.REQUEST);
        if (target.contains("/")) reqAsdu.lnReference(target);
        else reqAsdu.ldName(target);
        reqAsdu.acsiClass.set(acsiClass);
        response = sendAndVerify(client, reqAsdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String target = stringVal("target");
        String type = stringVal("type");
        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        List<CmsCBValueEntry> entries = asdu.cbValue().toList();
        CliPrinter.printList("CB values (" + entries.size() + " entries)", entries,
                item -> item.reference().get() + " = " + item.value());

        if ("SGCB".equalsIgnoreCase(type)) {
            String ldName = target.contains("/") ? target.substring(0, target.indexOf('/')) : target;
            for (CmsCBValueEntry entry : entries) {
                CmsSGCB sgb = entry.value().sgb;
                String sgcbRef = ldName + "/" + entry.reference().get();
                ctx.updateSgcbAttribute(sgcbRef, "sgcbName", sgb.sgcbName.get());
                ctx.updateSgcbAttribute(sgcbRef, "sgcbRef", sgb.sgcbRef.get());
                ctx.updateSgcbAttribute(sgcbRef, "numOfSG", String.valueOf(sgb.numOfSG.get()));
                ctx.updateSgcbAttribute(sgcbRef, "actSG", String.valueOf(sgb.actSG.get()));
                ctx.updateSgcbAttribute(sgcbRef, "editSG", String.valueOf(sgb.editSG.get()));
                ctx.updateSgcbAttribute(sgcbRef, "cnfEdit", String.valueOf(sgb.cnfEdit.get()));
                ctx.updateSgcbAttribute(sgcbRef, "lActTm", String.valueOf(sgb.lActTm.secondsSinceEpoch.get()));
                ctx.updateSgcbAttribute(sgcbRef, "resvTms", String.valueOf(sgb.resvTms.get()));
            }
        } else if ("BRCB".equalsIgnoreCase(type)) {
            String ldName = target.contains("/") ? target.substring(0, target.indexOf('/')) : target;
            for (CmsCBValueEntry entry : entries) {
                CmsBRCB brcb = entry.value().brcb;
                String ref = ldName + "/" + entry.reference().get();
                ctx.updateBrcbAttribute(ref, "brcbName", brcb.brcbName.get());
                ctx.updateBrcbAttribute(ref, "brcbRef", brcb.brcbRef.get());
                ctx.updateBrcbAttribute(ref, "rptID", brcb.rptID.get());
                ctx.updateBrcbAttribute(ref, "rptEna", String.valueOf(brcb.rptEna.get()));
                ctx.updateBrcbAttribute(ref, "datSet", brcb.datSet.get());
                ctx.updateBrcbAttribute(ref, "confRev", String.valueOf(brcb.confRev.get()));
                ctx.updateBrcbAttribute(ref, "optFlds", String.valueOf(brcb.optFlds.get()));
                ctx.updateBrcbAttribute(ref, "bufTm", String.valueOf(brcb.bufTm.get()));
                ctx.updateBrcbAttribute(ref, "sqNum", String.valueOf(brcb.sqNum.get()));
                ctx.updateBrcbAttribute(ref, "intgPd", String.valueOf(brcb.intgPd.get()));
                ctx.updateBrcbAttribute(ref, "gi", String.valueOf(brcb.gi.get()));
                ctx.updateBrcbAttribute(ref, "purgeBuf", String.valueOf(brcb.purgeBuf.get()));
            }
        } else if ("URCB".equalsIgnoreCase(type)) {
            String ldName = target.contains("/") ? target.substring(0, target.indexOf('/')) : target;
            for (CmsCBValueEntry entry : entries) {
                CmsURCB urcb = entry.value().urcb;
                String ref = ldName + "/" + entry.reference().get();
                ctx.updateUrcbAttribute(ref, "urcbName", urcb.urcbName.get());
                ctx.updateUrcbAttribute(ref, "urcbRef", urcb.urcbRef.get());
                ctx.updateUrcbAttribute(ref, "rptID", urcb.rptID.get());
                ctx.updateUrcbAttribute(ref, "rptEna", String.valueOf(urcb.rptEna.get()));
                ctx.updateUrcbAttribute(ref, "resv", String.valueOf(urcb.resv.get()));
                ctx.updateUrcbAttribute(ref, "datSet", urcb.datSet.get());
                ctx.updateUrcbAttribute(ref, "confRev", String.valueOf(urcb.confRev.get()));
                ctx.updateUrcbAttribute(ref, "optFlds", String.valueOf(urcb.optFlds.get()));
                ctx.updateUrcbAttribute(ref, "bufTm", String.valueOf(urcb.bufTm.get()));
                ctx.updateUrcbAttribute(ref, "sqNum", String.valueOf(urcb.sqNum.get()));
                ctx.updateUrcbAttribute(ref, "intgPd", String.valueOf(urcb.intgPd.get()));
                ctx.updateUrcbAttribute(ref, "gi", String.valueOf(urcb.gi.get()));
            }
        }

        if (target.contains("/") && !target.toUpperCase().contains("/LLN0")) {
            CliPrinter.info("控制块只能在 LLN0 下查询");
        }
    }

    private int parseAcsi(String s) {
        if (s == null || s.isEmpty()) return CmsACSIClass.URCB;
        switch (s.toUpperCase()) {
            case "BRCB": return CmsACSIClass.BRCB;
            case "URCB": return CmsACSIClass.URCB;
            case "LCB": return CmsACSIClass.LCB;
            case "GO_CB": return CmsACSIClass.GO_CB;
            case "MSV_CB": return CmsACSIClass.MSV_CB;
            case "SGCB": return CmsACSIClass.SGCB;
            default: return CmsACSIClass.URCB;
        }
    }
}
