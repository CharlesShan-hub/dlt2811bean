package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.utils.scl2.model.template.SclDA;
import com.ysh.jcms.utils.scl2.model.template.SclDOType;
import com.ysh.jcms.utils.scl2.model.template.SclSDO;
import com.ysh.jcms.utils.scl2.navigate.CmsDataTypeMap;
import com.ysh.jcms.utils.scl2.navigate.Navigator;
import com.ysh.jcms.utils.scl2.navigate.TypeChain;

/**
 * 数据定义解析器。
 * <p>
 * 按引用路径查找数据类型定义，返回 {@link DataDefinitionEntry}。
 * 基于 {@link Navigator} + {@link TypeChain} 积木。
 */
public final class DataDefinitionResolver {

    private DataDefinitionResolver() {}

    // ==================== 核心入口 ====================

    /**
     * 按完整引用解析数据定义。
     */
    public static DataDefinitionEntry resolve(Navigator nav) {
        return resolve(nav, null);
    }

    /**
     * 按完整引用解析数据定义，支持 FC 过滤。
     */
    public static DataDefinitionEntry resolve(Navigator nav, String fc) {
        if (!nav.isValid() || nav.ln() == null) return null;
        if (nav.ref().isLnLevel()) return null;

        if (!applyFcFilter(nav, fc)) return null;

        if (nav.ref().isDoLevel()) return resolveDoLevel(nav);
        if (nav.ref().isDaLevel()) return resolveDaLevel(nav);

        return null;
    }

    /** FC 过滤检查 */
    private static boolean applyFcFilter(Navigator nav, String fc) {
        if (fc == null || fc.isEmpty() || "XX".equals(fc)) return true;
        if (nav.document().dataTypeTemplates() == null) return false;

        TypeChain.DoTypeStep step = TypeChain.of(nav.document().dataTypeTemplates())
                .from(nav.ln().lnType())
                .doDef(nav.ref().doName());
        SclDOType doType = step.doType();
        if (doType == null) return false;

        if (nav.ref().isDaLevel()) {
            String daName = nav.dai() != null ? nav.dai().name() : nav.ref().daName();
            SclDA da = doType.findDaByName(daName);
            return da != null && fc.equalsIgnoreCase(da.fc());
        }

        for (SclDA da : doType.das()) {
            if (fc.equalsIgnoreCase(da.fc())) return true;
        }
        return false;
    }

    /** DA / SDI.BDA 级别 */
    private static DataDefinitionEntry resolveDaLevel(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null) return null;

        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain()) ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());

        String bType = TypeChain.of(nav.document().dataTypeTemplates())
                .resolveBType(nav.ln().lnType(), ref.toString());
        if (bType == null) return null;
        return new DataDefinitionEntry(nav.ref().fullReference(), "", toDataDefinition(bType));
    }

    /** DO 级别：CDC 类型 + 结构定义 */
    private static DataDefinitionEntry resolveDoLevel(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null) return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());

        String cdc = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).cdc();
        CmsDataDefinition def = buildDoDefinition(nav);
        if (def == null) return null;
        return new DataDefinitionEntry(nav.ref().fullReference(), cdc != null ? cdc : "SPC", def);
    }

    /** 构造 DO 级别的结构定义 */
    private static CmsDataDefinition buildDoDefinition(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null) return null;
        TypeChain.DoTypeStep step = TypeChain.of(nav.document().dataTypeTemplates())
                .from(nav.ln().lnType())
                .doDef(nav.ref().doName());
        SclDOType doType = step.doType();
        if (doType == null) return null;

        CmsArray<CmsDataDefinitionStructElem> arr = new CmsArray<>();
        for (SclDA da : doType.das()) {
            arr.add(new CmsDataDefinitionStructElem()
                    .name(da.name())
                    .fc(da.fc() != null ? CmsFC.fromCode(da.fc()) : 0)
                    .type(toDataDefinition(da.bType())));
        }
        for (SclSDO sdo : doType.sdos()) {
            arr.add(new CmsDataDefinitionStructElem()
                    .name(sdo.name())
                    .fc(0)
                    .type(nullDataDefinition()));
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(CmsDataTypeMap.SEL_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    // ==================== bType → CmsDataDefinition ====================

    /** bType 转 CmsDataDefinition（含长度约束） */
    public static CmsDataDefinition toDataDefinition(String bType) {
        if (bType == null) return nullDataDefinition();
        switch (bType.toUpperCase()) {
            case "BOOLEAN":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            case "INT8":         return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8);
            case "INT16":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16);
            case "INT32":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32);
            case "INT64":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64);
            case "INT8U":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8U);
            case "INT16U":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16U);
            case "INT32U":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32U);
            case "INT64U":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64U);
            case "FLOAT32":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT32);
            case "FLOAT64":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT64);
            case "BIT_STRING":
            case "BITSTRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BIT_STRING);
                def.alt_bit_string_len.value(0);
                return def;
            }
            case "OCTET_STRING":
            case "OCTETSTRING":
            case "VISSTRING255":
            case "VISIBLE_STRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-255);
                return def;
            }
            case "UNICODE_STRING":
            case "UNICODESTRING":
            case "UNICODE255": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UNICODE_STRING);
                def.alt_unicode_string_len.value(-255);
                return def;
            }
            case "VISSTRING64": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-64);
                return def;
            }
            case "UTC_TIME":
            case "UTCTIME":
            case "TIMESTAMP":    return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UTC_TIME);
            case "BINARY_TIME":
            case "BINARYTIME":
            case "ENTRYTIME":    return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BINARY_TIME);
            case "QUALITY":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_QUALITY);
            case "DBPOS":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_DBPOS);
            case "TCMD":         return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_TCMD);
            case "CHECK":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_CHECK);
            case "STRUCT":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            default:             return nullDataDefinition();
        }
    }

    private static CmsDataDefinition nullDataDefinition() {
        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
    }
}
