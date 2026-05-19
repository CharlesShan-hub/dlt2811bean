package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
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
        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        List<CmsCBValueEntry> entries = asdu.cbValue().toList();
        CliPrinter.printList("CB values (" + entries.size() + " entries)", entries,
                item -> item.reference().get() + " = " + item.value());
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
