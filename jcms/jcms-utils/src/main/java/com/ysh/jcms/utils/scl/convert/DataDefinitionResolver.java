package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.CmsDataTypeMap;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据定义解析器。
 * <p>
 * 按引用路径查找数据类型定义，返回 {@link DataDefinitionEntry}。
 * 基于 {@link Navigator} + {@link TypeChain} 积木。
 */
public final class DataDefinitionResolver {

    private static final Logger log = LoggerFactory.getLogger(DataDefinitionResolver.class);

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
        log.warn("resolve ENTER: navValid={} ln={} ref='{}'",
            nav.isValid(),
            nav.ln() != null ? nav.ln().getFullName() : "null",
            nav.ref() != null ? nav.ref().fullReference() : "null");
        if (!nav.isValid() || nav.ln() == null) {
            log.warn("resolve: nav invalid or ln=null");
            return null;
        }
        if (nav.ref().isLnLevel()) {
            log.warn("resolve: ln level, no definition");
            return null;
        }

        if (!applyFcFilter(nav, fc)) {
            log.warn("resolve: fc filter failed fc={}", fc);
            return null;
        }

        if (nav.ref().isDoLevel()) return resolveDoLevel(nav);
        if (nav.ref().isDaLevel()) return resolveDaLevel(nav);

        log.warn("resolve: neither DO nor DA level (do={} da={})", nav.ref().isDoLevel(), nav.ref().isDaLevel());
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
        log.warn("resolveDaLevel ENTER: ref='{}' doName={} daName={} templates={} lnType={}",
            nav.ref().fullReference(),
            nav.ref().doName(),
            nav.ref().daName(),
            nav.document().dataTypeTemplates() != null ? "ok" : "null",
            nav.ln().lnType());
        if (nav.document().dataTypeTemplates() == null) {
            log.warn("resolveDaLevel: dataTypeTemplates=null");
            return null;
        }

        String daName = nav.ref().daName();
        // 从模板中查找 DA 定义（走 TypeChain）
        String lnTypeId = nav.ln().lnType();
        TypeChain ta = TypeChain.of(nav.document().dataTypeTemplates());
        String fullRef = nav.ref().doName() + "." + daName;
        TypeChain.DaStep daStep = ta.from(lnTypeId).doDef(nav.ref().doName()).daDef(daName);
        SclDA da = daStep != null ? daStep.da() : null;
        if (da != null) {
            log.warn("resolveDaLevel DA: name={} bType={} fc={} sAddr={} dchg={} dupd={} type={}",
                da.name(), da.bType(), da.fc(), da.sAddr(), da.dchg(), da.dupd(), da.type());
        } else {
            log.warn("resolveDaLevel: DA '{}' not found in DOType for lnType={}", daName, lnTypeId);
        }

        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain()) ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());

        String bType = TypeChain.of(nav.document().dataTypeTemplates())
                .resolveBType(nav.ln().lnType(), ref.toString());
        if (bType == null) {
            log.warn("resolveDaLevel: bType null for ref={}, lnType={}", ref, nav.ln().lnType());
            return null;
        }
        return new DataDefinitionEntry(nav.ref().fullReference(), "", toDataDefinition(bType));
    }

    /** DO 级别：CDC 类型 + 结构定义 */
    private static DataDefinitionEntry resolveDoLevel(Navigator nav) {
        log.warn("resolveDoLevel ENTER: ref='{}' doName={} templates={} lnType={}",
            nav.ref().fullReference(), nav.ref().doName(),
            nav.document().dataTypeTemplates() != null ? "ok" : "null",
            nav.ln().lnType());
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
        // 统一转小写、去下划线、去前后空格，避免大小写/格式变体
        String key = bType.trim().replace("_", "").replace("-", "").toLowerCase();
        switch (key) {
            case "boolean":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            case "int8":         return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8);
            case "int16":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16);
            case "int32":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32);
            case "int64":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64);
            case "int8u":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8U);
            case "int16u":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16U);
            case "int32u":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32U);
            case "int64u":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64U);
            case "enum":         return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32);
            case "float32":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT32);
            case "float64":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT64);
            case "bitstring": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BIT_STRING);
                def.alt_bit_string_len.value(0);
                return def;
            }
            case "octetstring":
            case "visstring255":
            case "visiblestring": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-255);
                return def;
            }
            case "unicodestring":
            case "unicode255": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UNICODE_STRING);
                def.alt_unicode_string_len.value(-255);
                return def;
            }
            case "visstring64": {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-64);
                return def;
            }
            case "utctime":
            case "timestamp":    return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UTC_TIME);
            case "binarytime":
            case "entrytime":    return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BINARY_TIME);
            case "quality":      return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_QUALITY);
            case "dbpos":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_DBPOS);
            case "tcmd":         return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_TCMD);
            case "check":        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_CHECK);
            case "struct":       return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            default:             return nullDataDefinition();
        }
    }

    private static CmsDataDefinition nullDataDefinition() {
        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
    }
}
