package com.ysh.jcms.utils.scl2.query;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl2.model.communication.SclConnectedAP;
import com.ysh.jcms.utils.scl2.model.communication.SclSubNetwork;
import com.ysh.jcms.utils.scl2.model.ied.*;
import com.ysh.jcms.utils.scl2.model.substation.SclSubstation;
import com.ysh.jcms.utils.scl2.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl2.ref.SclRef;
import com.ysh.jcms.utils.scl2.ref.SclRefParser;

import java.util.Collections;
import java.util.List;

/**
 * SCL 文档的查询门面。
 * <p>
 * 统一入口，提供对 SclDocument 各模块的结构化查询。
 */
public class SclQuery {

    private final SclDocument document;

    public SclQuery(SclDocument document) {
        this.document = document;
    }

    // ==================== 顶层 ====================

    public SclDocument document() {
        return document;
    }

    // ==================== IED 查询 ====================

    public List<SclIED> ieds() {
        return document != null ? document.ieds() : Collections.emptyList();
    }

    public SclIED ied(String name) {
        return document != null ? document.findIedByName(name) : null;
    }

    /** 返回作用域到指定 IED 的查询对象 */
    public IedQuery iedQuery(String name) {
        SclIED ied = ied(name);
        return ied != null ? new IedQuery(ied) : null;
    }

    // ==================== DataType 查询 ====================

    public DataTypeQuery dataTypes() {
        SclDataTypeTemplates templates = document != null ? document.dataTypeTemplates() : null;
        return templates != null ? new DataTypeQuery(templates) : null;
    }

    // ==================== Communication 查询 ====================

    public SclCommunication communication() {
        return document != null ? document.communication() : null;
    }

    public List<SclSubNetwork> subNetworks() {
        SclCommunication comm = communication();
        return comm != null ? comm.subNetworks() : Collections.emptyList();
    }

    public SclSubNetwork subNetwork(String name) {
        SclCommunication comm = communication();
        return comm != null ? comm.findSubNetworkByName(name) : null;
    }

    public List<SclConnectedAP> connectedAPs(String subNetworkName) {
        SclSubNetwork sn = subNetwork(subNetworkName);
        return sn != null ? sn.connectedAPs() : Collections.emptyList();
    }

    // ==================== Substation 查询 ====================

    public SclSubstation substation() {
        return document != null ? document.substation() : null;
    }

    // ==================== 便捷查询 ====================

    /**
     * 按完整引用字符串查找 LN。
     * <p>
     * 引用格式：{@code IEDName/LD/LN}，例如 {@code E1Q1SB1/C1/MMXU1}
     */
    public SclLN findLnByFullRef(String fullRef) {
        if (fullRef == null || document == null) return null;
        // 格式: IEDName/LD/LNClass+Inst
        int firstSlash = fullRef.indexOf('/');
        if (firstSlash <= 0) return null;
        String iedName = fullRef.substring(0, firstSlash);
        String rest = fullRef.substring(firstSlash + 1);

        IedQuery iq = iedQuery(iedName);
        return iq != null ? iq.findLnByRef(rest) : null;
    }

    /**
     * 解析数据引用的 bType。
     * <p>
     * 引用格式：{@code IEDName/LD/LN.DO.DA}
     * 自动查找 IED → LDevice → LN → lnType → LNodeType → DO → DOType → DA → bType。
     */
    public String resolveBType(String fullRef) {
        if (fullRef == null || document == null) return null;
        if (!SclRefParser.isValid(fullRef)) return null;

        // 格式: IEDName/LD/LN.DO.DA
        // 先拆出 IEDName，剩下的交给 SclRefParser
        int firstSlash = fullRef.indexOf('/');
        if (firstSlash <= 0) return null;
        String iedName = fullRef.substring(0, firstSlash);
        String refPart = fullRef.substring(firstSlash + 1);

        SclIED ied = ied(iedName);
        if (ied == null) return null;

        SclRef sclRef = SclRefParser.parse(refPart);
        if (sclRef.isLnLevel()) return null;

        // 找到 LN 的 lnType
        IedQuery iq = iedQuery(iedName);
        SclLN ln = iq != null ? iq.findLnByRef(refPart) : null;
        if (ln == null || ln.lnType() == null) return null;

        DataTypeQuery dtq = dataTypes();
        return dtq != null ? dtq.resolveBType(ln.lnType(), refPart) : null;
    }

    // ==================== 工具 ====================

    /** 跨所有 IED 查找包含指定 LDevice inst 的 IED */
    public SclIED findIedByLdInst(String ldInst) {
        if (document == null) return null;
        for (SclIED ied : document.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer server = ap.server();
                if (server != null && server.findLDeviceByInst(ldInst) != null) {
                    return ied;
                }
            }
        }
        return null;
    }
}
