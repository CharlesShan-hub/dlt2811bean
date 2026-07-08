package com.ysh.jcms.utils.scl2.navigate;

import com.ysh.jcms.utils.scl2.model.template.*;

/**
 * 类型链追溯器。
 * <p>
 * 从 LN 的 lnType 出发，沿 {@code LNodeType → DO → DOType → DA → bType} 链追溯。
 * 支持 SDI/BDA 嵌套追溯。
 * <p>
 * 用法：
 * <pre>{@code
 * String bType = TypeChain.of(templates)
 *     .from(lnTypeId).doDef("Mod").daDef("stVal").bType();
 * // → "Enum"
 * }</pre>
 */
public class TypeChain {

    private final SclDataTypeTemplates templates;

    private TypeChain(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    public static TypeChain of(SclDataTypeTemplates templates) {
        if (templates == null) throw new IllegalArgumentException("templates must not be null");
        return new TypeChain(templates);
    }

    // ==================== Step Builder ====================

    /** 第一步：从 LNodeType 开始。返回 DO 选择器。*/
    public DoStep from(String lnTypeId) {
        SclLNodeType lnt = templates.findLNodeTypeById(lnTypeId);
        return new DoStep(lnt);
    }

    // ==================== Step 类 ====================

    /** DO 选择步骤 */
    public class DoStep {
        private final SclLNodeType lnt;
        DoStep(SclLNodeType lnt) { this.lnt = lnt; }

        /** 按名称查找 DO 定义 */
        public DoTypeStep doDef(String doName) {
            if (lnt == null) return new DoTypeStep(null);
            SclDO doDef = lnt.findDoByName(doName);
            if (doDef == null || doDef.type() == null) return new DoTypeStep(null);
            SclDOType doType = templates.findDoTypeById(doDef.type());
            return new DoTypeStep(doType);
        }
    }

    /** DOType 选择步骤 */
    public class DoTypeStep {
        private final SclDOType doType;
        DoTypeStep(SclDOType doType) { this.doType = doType; }

        public SclDOType doType() { return doType; }
        public String cdc() { return doType != null ? doType.cdc() : null; }

        /** 按名称查找 DA 定义 */
        public DaStep daDef(String daName) {
            if (doType == null) return new DaStep(null);
            SclDA da = doType.findDaByName(daName);
            return new DaStep(da);
        }
    }

    /** DA 选择步骤 */
    public class DaStep {
        private final SclDA da;
        DaStep(SclDA da) { this.da = da; }

        public SclDA da() { return da; }
        public String bType() { return da != null ? da.bType() : null; }
        public String fc() { return da != null ? da.fc() : null; }

        /** 如果 bType 是 Struct，继续追 DAType */
        public BdaStep daType() {
            if (da != null && "Struct".equals(da.bType()) && da.type() != null) {
                SclDAType dat = templates.findDaTypeById(da.type());
                return new BdaStep(dat);
            }
            return new BdaStep(null);
        }
    }

    /** BDA 选择步骤（DAType 内部） */
    public class BdaStep {
        private final SclDAType dat;
        BdaStep(SclDAType dat) { this.dat = dat; }

        public SclDAType dat() { return dat; }

        /** 按名称查找 BDA */
        public SclBDA bdaDef(String bdaName) {
            if (dat == null) return null;
            return dat.findBdaByName(bdaName);
        }

        /** 第一个 BDA 的 bType（适用于单字段结构体） */
        public String firstBdaBType() {
            if (dat == null || dat.bdas().isEmpty()) return null;
            return dat.bdas().get(0).bType();
        }
    }

    // ==================== 快捷方法 ====================

    /**
     * 快捷解析：从 lnType 开始，一步解析引用到 bType。
     * <p>
     * 引用格式：{@code DO.DA} 或 {@code DO.SDI.BDA}
     *
     * @param lnTypeId LNodeType 的 id
     * @param ref      DO 级别引用（如 "Mod.stVal"）
     * @return bType 字符串，无法解析返回 null
     */
    public String resolveBType(String lnTypeId, String ref) {
        if (ref == null) return null;
        String[] parts = ref.split("\\.");
        if (parts.length < 1) return null;

        SclLNodeType lnt = templates.findLNodeTypeById(lnTypeId);
        if (lnt == null) return null;

        // DO
        SclDO doDef = lnt.findDoByName(parts[0]);
        if (doDef == null || doDef.type() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.type());
        if (doType == null) return null;

        if (parts.length == 1) {
            return doType.cdc();
        }

        if (parts.length == 2) {
            // DO.DA
            SclDA da = doType.findDaByName(parts[1]);
            return da != null ? da.bType() : null;
        }

        // DO.SDI[.SDI].BDA
        for (int i = 1; i < parts.length - 1; i++) {
            SclDA da = doType.findDaByName(parts[i]);
            if (da == null || !"Struct".equals(da.bType()) || da.type() == null) return null;
            SclDAType dat = templates.findDaTypeById(da.type());
            if (dat == null) return null;
            // 下一级：继续在 DAType 中查找同名的 DA 作为下一层 SDI
            // 但标准中 SDI 对应 DOType 里的 DA，而不是 DA 嵌套
            // 实际 SDI 链是这样：DOType.DA(name="sVC", bType=Struct, type="ScaledValueConfig")
            // → DAType("ScaledValueConfig").BDA(name="scaleFactor")
            // 所以对于 DO.SDI.BDA，我们需要找到 DOType 中 name=sdiName 的 DA
            // 然后到 DAType 中找 name=bdaName 的 BDA
        }

        // 最后一段是 BDA 名
        int lastDot = ref.lastIndexOf('.');
        String prefix = ref.substring(0, lastDot);
        String bdaName = ref.substring(lastDot + 1);

        // 追溯 SDI 链
        String[] doParts = prefix.split("\\.");
        SclDOType currentDoType = doType;
        SclDAType currentDat = null;

        // doParts[0] 是 DO 名，跳过。从 doParts[1] 开始是 SDI 名
        for (int i = 1; i < doParts.length; i++) {
            SclDA sdiDa = currentDoType.findDaByName(doParts[i]);
            if (sdiDa == null || !"Struct".equals(sdiDa.bType()) || sdiDa.type() == null) return null;
            currentDat = templates.findDaTypeById(sdiDa.type());
            if (currentDat == null) return null;
            // 最后一级用 DAType 查 BDA
        }

        SclBDA bda = currentDat.findBdaByName(bdaName);
        return bda != null ? bda.bType() : null;
    }
}
