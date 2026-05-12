package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class LnDirHandler extends AbstractServiceHandler {

    public LnDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LOGIC_NODE_DIRECTORY); }
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1"),
            new Param("acsi", "ACSI 类", "DATA_OBJECT", List.of(
                new Param.EnumChoice("DATA_OBJECT", "数据对象"),
                new Param.EnumChoice("DATA_SET", "数据集"),
                new Param.EnumChoice("BRCB", "报告控制块（缓存）"),
                new Param.EnumChoice("URCB", "报告控制块（非缓存）"),
                new Param.EnumChoice("LCB", "日志控制块"),
                new Param.EnumChoice("LOG", "日志"),
                new Param.EnumChoice("SGCB", "定值组控制块"),
                new Param.EnumChoice("GO_CB", "GOOSE 控制块"),
                new Param.EnumChoice("MSV_CB", "采样值控制块")
            ))
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String target = values.get("target");
        int acsiClass = parseAcsi(values.get("acsi"));
        CmsGetLogicalNodeDirectory reqAsdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        reqAsdu.acsiClass(new CmsACSIClass(acsiClass));
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        if (!printIfEmpty(asdu.referenceResponse().isEmpty())) {
            System.out.println("  Entries:");
            for (int i = 0; i < asdu.referenceResponse().size(); i++) {
                System.out.println("    [" + i + "] " + asdu.referenceResponse().get(i).get());
            }
        }
    }
    private int parseAcsi(String s) {
        switch (s.toUpperCase()) {
            case "DATA_SET": return CmsACSIClass.DATA_SET;
            case "BRCB": return CmsACSIClass.BRCB;
            case "URCB": return CmsACSIClass.URCB;
            case "LCB": return CmsACSIClass.LCB;
            case "LOG": return CmsACSIClass.LOG;
            case "SGCB": return CmsACSIClass.SGCB;
            case "GO_CB": return CmsACSIClass.GO_CB;
            case "MSV_CB": return CmsACSIClass.MSV_CB;
            default: return CmsACSIClass.DATA_OBJECT;
        }
    }
}
