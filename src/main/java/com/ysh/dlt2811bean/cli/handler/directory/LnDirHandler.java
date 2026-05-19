package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsScalar;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LnDirHandler extends AbstractServiceHandler {

    private String target;
    private int acsiClass;

    public LnDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LOGIC_NODE_DIRECTORY); }

    protected List<Param> setParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
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
            )),
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.REFERENCE)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {

        target = stringVal("target");
        acsiClass = parseAcsi(stringVal("acsi"));
        String referenceAfter = stringVal("referenceAfter");

        CmsGetLogicalNodeDirectory asdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST)
            .acsiClass(new CmsACSIClass(acsiClass));
        if (target.contains("/"))
            asdu.lnReference(target);
        else
            asdu.ldName(target);
        if (!referenceAfter.isEmpty())
            asdu.referenceAfter(referenceAfter);
        response = client.send(asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            CliPrinter.error("GetLogicalNodeDirectory failed");
            return;
        }
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {

        CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
        List<String> refs = asdu.referenceResponse().stream().map(AbstractCmsScalar::get).collect(Collectors.toList());
        CliPrinter.printList("Entries", refs, item -> item);
        if (acsiClass == CmsACSIClass.SGCB)
            CliPrinter.info("Note: SG names are fixed (SG1/SG2...), cannot read from SCD file");
        if (!refs.isEmpty() && target.contains("/")) {
            refs.forEach(r -> ctx.addAcdMember(target, val("acsi"), r));
        }
    }

    private int parseAcsi(String s) {
        return switch (s.toUpperCase()) {
            case "DATA_SET" -> CmsACSIClass.DATA_SET;
            case "BRCB" -> CmsACSIClass.BRCB;
            case "URCB" -> CmsACSIClass.URCB;
            case "LCB" -> CmsACSIClass.LCB;
            case "LOG" -> CmsACSIClass.LOG;
            case "SGCB" -> CmsACSIClass.SGCB;
            case "GO_CB" -> CmsACSIClass.GO_CB;
            case "MSV_CB" -> CmsACSIClass.MSV_CB;
            default -> CmsACSIClass.DATA_OBJECT;
        };
    }
}
