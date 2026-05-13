package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllCbHandler extends AbstractServiceHandler {

    public GetAllCbHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_CB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1"),
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

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String target = values.get("target");
        int acsiClass = parseAcsi(values.get("type"));
        CmsGetAllCBValues reqAsdu = new CmsGetAllCBValues(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        reqAsdu.acsiClass.set(acsiClass);
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
        List<CmsCBValueEntry> entries = asdu.cbValue().toList();
        CliPrinter.printList("CB values (" + entries.size() + " entries)", entries,
                item -> item.reference().get() + " = " + item.value());
    }
    
    private int parseAcsi(String s) {
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
