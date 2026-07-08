package com.ysh.jcms.utils.scl2.query;

import com.ysh.jcms.utils.scl2.model.template.*;
import com.ysh.jcms.utils.scl2.ref.SclRef;
import com.ysh.jcms.utils.scl2.ref.SclRefParser;

/**
 * DataTypeTemplates 作用域下的查询门面。
 */
public class DataTypeQuery {

    private final SclDataTypeTemplates templates;

    public DataTypeQuery(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    /** 返回当前 DataTypeTemplates（可能为 null） */
    public SclDataTypeTemplates templates() {
        return templates;
    }

    // ==================== LNodeType ====================

    public SclLNodeType lNodeType(String id) {
        return templates != null ? templates.findLNodeTypeById(id) : null;
    }

    public SclDOType doType(String id) {
        return templates != null ? templates.findDoTypeById(id) : null;
    }

    public SclDAType daType(String id) {
        return templates != null ? templates.findDaTypeById(id) : null;
    }

    public SclEnumType enumType(String id) {
        return templates != null ? templates.findEnumTypeById(id) : null;
    }

    // ==================== 类型解析 ====================

    /**
     * 在指定 LNodeType 中查找 DO 定义。
     *
     * @param lnTypeId LNodeType 的 id
     * @param doName   DO 名称
     * @return SclDO 实例，未找到返回 null
     */
    public SclDO findDoInLNodeType(String lnTypeId, String doName) {
        SclLNodeType lnt = lNodeType(lnTypeId);
        if (lnt == null) return null;
        for (SclDO doDef : lnt.dos()) {
            if (doDef.name().equals(doName)) {
                return doDef;
            }
        }
        return null;
    }

    /**
     * 在指定 DOType 中查找 DA 定义。
     *
     * @param doTypeId DOType 的 id
     * @param daName   DA 名称
     * @return SclDA 实例，未找到返回 null
     */
    public SclDA findDaInDoType(String doTypeId, String daName) {
        SclDOType dot = doType(doTypeId);
        if (dot == null) return null;
        for (SclDA da : dot.das()) {
            if (da.name().equals(daName)) {
                return da;
            }
        }
        return null;
    }

    /**
     * 解析一个数据引用的 bType（基础类型）。
     * <p>
     * 引用格式：{@code LD/LN.DO.DA} 或 {@code LD/LN.DO}
     * 通过 LN 的 lnType → LNodeType → DO → DOType → DA → bType 追溯。
     *
     * @param lnTypeId 所属 LNodeType 的 id
     * @param ref      数据引用路径
     * @return bType 字符串，无法解析返回 null
     */
    public String resolveBType(String lnTypeId, String ref) {
        if (!SclRefParser.isValid(ref)) return null;
        SclRef sclRef = SclRefParser.parse(ref);

        // 查找 DO 定义
        SclDO doDef = findDoInLNodeType(lnTypeId, sclRef.doName());
        if (doDef == null) return null;

        // 通过 DO 的 type 找 DOType
        SclDOType doType = doType(doDef.type());
        if (doType == null) return null;

        if (sclRef.isDoLevel()) {
            // DO 级别返回 cdc
            return doType.cdc();
        }

        // DA 级别：在 DOType 中查找 DA
        SclDA da = findDaInDoType(doDef.type(), sclRef.daName());
        if (da == null) return null;

        // 如果 bType 是 Struct，继续往下追 DAType
        if ("Struct".equals(da.bType()) && da.type() != null) {
            SclDAType daType = daType(da.type());
            if (daType != null && !daType.bdas().isEmpty()) {
                return daType.bdas().get(0).bType();
            }
        }

        return da.bType();
    }
}
