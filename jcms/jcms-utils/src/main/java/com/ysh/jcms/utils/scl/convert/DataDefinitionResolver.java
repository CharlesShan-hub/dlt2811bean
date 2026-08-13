package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.sequence.common.CmsDataDefinitionStructElem;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.CmsDataTypeMap;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Data definition resolver.
 * <p>
 * Looks up the data type definition by reference path and returns a {@link DataDefinitionEntry}. Based on {@link Navigator} +
 * {@link TypeChain} building blocks.
 */
public final class DataDefinitionResolver {

    private static final Logger log = LoggerFactory.getLogger(DataDefinitionResolver.class);

    private DataDefinitionResolver() {
    }

    // ==================== Core entry ====================

    /**
     * Resolves the data definition by a full reference.
     */
    public static DataDefinitionEntry resolve(Navigator nav) {
        return resolve(nav, null);
    }

    /**
     * Resolves the data definition by a full reference, with FC filtering support.
     */
    public static DataDefinitionEntry resolve(Navigator nav, String fc) {
        if (!nav.isValid() || nav.ln() == null) {
            log.debug("resolve: nav invalid or ln=null");
            return null;
        }
        if (nav.ref().isLnLevel()) {
            log.debug("resolve: ln level, no definition");
            return null;
        }

        if (!applyFcFilter(nav, fc)) {
            log.debug("resolve: fc filter failed fc={}", fc);
            return null;
        }

        if (nav.ref().isDoLevel())
            return resolveDoLevel(nav);
        if (nav.ref().isDaLevel())
            return resolveDaLevel(nav);

        log.debug("resolve: neither DO nor DA level (do={} da={})", nav.ref().isDoLevel(), nav.ref().isDaLevel());
        return null;
    }

    /** FC filter check */
    private static boolean applyFcFilter(Navigator nav, String fc) {
        if (fc == null || fc.isEmpty() || "XX".equals(fc))
            return true;
        if (nav.document().dataTypeTemplates() == null)
            return false;

        TypeChain.DoTypeStep step = TypeChain.of(nav.document().dataTypeTemplates()).from(nav.ln().lnType()).doDef(nav.ref().doName());
        SclDOType doType = step.doType();
        if (doType == null)
            return false;

        if (nav.ref().isDaLevel()) {
            String daName = nav.dai() != null ? nav.dai().name() : nav.ref().daName();
            SclDA da = doType.findDaByName(daName);
            return da != null && fc.equalsIgnoreCase(da.fc());
        }

        for (SclDA da : doType.das()) {
            if (fc.equalsIgnoreCase(da.fc()))
                return true;
        }
        return false;
    }

    /** DA / SDI.BDA level */
    private static DataDefinitionEntry resolveDaLevel(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null) {
            log.debug("resolveDaLevel: dataTypeTemplates=null");
            return null;
        }

        String daName = nav.ref().daName();
        // Look up the DA definition from templates (via TypeChain)
        String lnTypeId = nav.ln().lnType();
        TypeChain ta = TypeChain.of(nav.document().dataTypeTemplates());
        String fullRef = nav.ref().doName() + "." + daName;
        TypeChain.DaStep daStep = ta.from(lnTypeId).doDef(nav.ref().doName()).daDef(daName);
        SclDA da = daStep != null ? daStep.da() : null;
        if (da != null) {
            log.debug("resolveDaLevel DA: name={} bType={} fc={} sAddr={} dchg={} dupd={} type={}", da.name(), da.bType(), da.fc(),
                    da.sAddr(), da.dchg(), da.dupd(), da.type());
        } else {
            log.warn("resolveDaLevel: DA '{}' not found in DOType for lnType={}", daName, lnTypeId);
        }

        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain())
            ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());

        String bType = TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref.toString());
        if (bType == null) {
            log.debug("resolveDaLevel: bType null for ref={}, lnType={}", ref, nav.ln().lnType());
            return null;
        }
        return new DataDefinitionEntry(nav.ref().fullReference(), "", toDataDefinition(bType));
    }

    /** DO level: CDC type + structure definition */
    private static DataDefinitionEntry resolveDoLevel(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());

        String cdc = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).cdc();
        CmsDataDefinition def = buildDoDefinition(nav);
        if (def == null)
            return null;
        return new DataDefinitionEntry(nav.ref().fullReference(), cdc != null ? cdc : "SPC", def);
    }

    /** Builds the structure definition at DO level */
    private static CmsDataDefinition buildDoDefinition(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null)
            return null;
        TypeChain.DoTypeStep step = TypeChain.of(nav.document().dataTypeTemplates()).from(nav.ln().lnType()).doDef(nav.ref().doName());
        SclDOType doType = step.doType();
        if (doType == null)
            return null;

        List<CmsDataDefinitionStructElem> arr = new ArrayList<>();
        for (SclDA da : doType.das()) {
            arr.add(new CmsDataDefinitionStructElem().name(da.name()).fc(da.fc() != null ? CmsFC.fromCode(da.fc()) : 0)
                    .type(toDataDefinition(da.bType())));
        }
        for (SclSDO sdo : doType.sdos()) {
            arr.add(new CmsDataDefinitionStructElem().name(sdo.name()).fc(0).type(nullDataDefinition()));
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(CmsDataTypeMap.SEL_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    // ==================== bType → CmsDataDefinition ====================

    /** Converts bType to CmsDataDefinition (with length constraints) */
    public static CmsDataDefinition toDataDefinition(String bType) {
        if (bType == null)
            return nullDataDefinition();
        // Normalize to lowercase, strip underscores and surrounding spaces to avoid case/format variants
        String key = bType.trim().replace("_", "").replace("-", "").toLowerCase();
        switch (key) {
            case "boolean" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            case "int8" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8);
            case "int16" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16);
            case "int32" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32);
            case "int64" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64);
            case "int8u" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT8U);
            case "int16u" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT16U);
            case "int32u" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32U);
            case "int64u" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT64U);
            case "enum" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_INT32);
            case "float32" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT32);
            case "float64" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_FLOAT64);
            case "bitstring" : {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BIT_STRING);
                def.alt_bit_string_len.value(0);
                return def;
            }
            case "octetstring" :
            case "visstring255" :
            case "visiblestring" : {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-255);
                return def;
            }
            case "unicodestring" :
            case "unicode255" : {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UNICODE_STRING);
                def.alt_unicode_string_len.value(-255);
                return def;
            }
            case "visstring64" : {
                CmsDataDefinition def = new CmsDataDefinition().choice(CmsDataTypeMap.SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-64);
                return def;
            }
            case "utctime" :
            case "timestamp" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_UTC_TIME);
            case "binarytime" :
            case "entrytime" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BINARY_TIME);
            case "quality" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_QUALITY);
            case "dbpos" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_DBPOS);
            case "tcmd" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_TCMD);
            case "check" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_CHECK);
            case "struct" :
                return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
            default :
                return nullDataDefinition();
        }
    }

    private static CmsDataDefinition nullDataDefinition() {
        return new CmsDataDefinition().choice(CmsDataTypeMap.SEL_BOOLEAN);
    }
}
