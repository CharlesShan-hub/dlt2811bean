package com.ysh.jcms.utils.scl.util;

import java.util.HashMap;
import java.util.Map;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.choice.CmsDataDefinitionStructElem;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.utils.scl.model.data.SclDataDefinitionEntry;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.model.ied.SclServer;

public class SclDataDefinitionResolver {

    private static final Map<String, Integer> FC_MAP = new HashMap<>();
    static {
        FC_MAP.put("ST", CmsFC.ST);
        FC_MAP.put("MX", CmsFC.MX);
        FC_MAP.put("SP", CmsFC.SP);
        FC_MAP.put("SV", CmsFC.SV);
        FC_MAP.put("CF", CmsFC.CF);
        FC_MAP.put("DC", CmsFC.DC);
        FC_MAP.put("SG", CmsFC.SG);
        FC_MAP.put("SE", CmsFC.SE);
        FC_MAP.put("SR", CmsFC.SR);
        FC_MAP.put("OR", CmsFC.OR);
        FC_MAP.put("BL", CmsFC.BL);
        FC_MAP.put("EX", CmsFC.EX);
        FC_MAP.put("XX", CmsFC.XX);
    }

    // Data definition selectors (alternatives 0..23)
    @SuppressWarnings("unused")
    private static final int SEL_ERROR          = 0;
    @SuppressWarnings("unused")
    private static final int SEL_ARRAY          = 1;
    private static final int SEL_STRUCTURE      = 2;
    private static final int SEL_BOOLEAN        = 3;
    private static final int SEL_INT8           = 4;
    private static final int SEL_INT16          = 5;
    private static final int SEL_INT32          = 6;
    private static final int SEL_INT64          = 7;
    private static final int SEL_INT8U          = 8;
    private static final int SEL_INT16U         = 9;
    private static final int SEL_INT32U         = 10;
    private static final int SEL_INT64U         = 11;
    private static final int SEL_FLOAT32        = 12;
    private static final int SEL_FLOAT64        = 13;
    private static final int SEL_BIT_STRING     = 14;
    @SuppressWarnings("unused")
    private static final int SEL_OCTET_STRING   = 15;
    private static final int SEL_VISIBLE_STRING = 16;
    private static final int SEL_UNICODE_STRING = 17;
    private static final int SEL_QUALITY        = 18;
    private static final int SEL_UTC_TIME       = 19;
    private static final int SEL_BINARY_TIME    = 20;
    private static final int SEL_DBPOS          = 21;
    private static final int SEL_TCMD           = 22;
    private static final int SEL_CHECK          = 23;

    /**
     * Resolves a data definition for a given reference and optional FC filter.
     */
    public static SclDataDefinitionEntry resolveDataDefinition(SclServer server, String ref, String fc, SclDataTypeTemplates templates) {
        if (ref == null || ref.isEmpty()) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 2) return null;

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return null;

        String lnName = parts[0];
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return null;

        String doName = parts[1];

        if (fc != null && !fc.isEmpty() && !"XX".equals(fc)) {
            if (templates == null) return null;
            if (parts.length > 2) {
                String daName = parts[parts.length - 1];
                String daFc = resolveDaFc(templates, ln, doName, daName);
                if (daFc == null || !daFc.equals(fc)) return null;
            } else {
                SclDOType doType = resolveDoType(templates, ln, doName);
                if (doType == null) return null;
                boolean hasFc = false;
                for (SclDA da : doType.getDas()) {
                    if (fc.equals(da.getFc())) {
                        hasFc = true;
                        break;
                    }
                }
                if (!hasFc) return null;
            }
        }

        if (parts.length > 2) {
            String daName = parts[parts.length - 1];
            if (parts.length == 3) {
                String bType = SclDataValueResolver.resolveDaBType(templates, ln, doName, daName);
                System.out.println(">>> resolveDataDefinition ref=" + ref + " bType=" + bType + " lnType=" + ln.getLnType());
                if (bType == null) return null;
                return new SclDataDefinitionEntry(ref, "", bTypeToDataDefinition(bType));
            }
            if (parts.length == 4) {
                String sdiName = parts[2];
                String bType = SclDataValueResolver.resolveSdiBdaBType(templates, ln, doName, sdiName, daName);
                if (bType == null) return null;
                return new SclDataDefinitionEntry(ref, "", bTypeToDataDefinition(bType));
            }
            return null;
        }

        String cdc = resolveCdc(templates, ln, doName);
        CmsDataDefinition doDef = buildDoDefinition(templates, ln, doName);
        if (doDef == null) return null;
        return new SclDataDefinitionEntry(ref, cdc != null ? cdc : "SPC", doDef);
    }

    private static String resolveCdc(SclDataTypeTemplates templates, SclLN ln, String doName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        return doType != null ? doType.getCdc() : null;
    }

    private static SclDOType resolveDoType(SclDataTypeTemplates templates, SclLN ln, String doName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        return templates.findDoTypeById(doDef.getType());
    }

    private static String resolveDaFc(SclDataTypeTemplates templates, SclLN ln, String doName, String daName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return null;
        SclDA da = doType.findDaByName(daName);
        return da != null ? da.getFc() : null;
    }

    private static CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclLN ln, String doName) {
        SclDOType doType = resolveDoType(templates, ln, doName);
        if (doType == null) return null;

        CmsArray<CmsDataDefinitionStructElem> arr = new CmsArray<>();
        for (SclDA da : doType.getDas()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(da.getName())
                .fc(da.getFc() != null ? FC_MAP.getOrDefault(da.getFc(), 0) : 0)
                .type(bTypeToDataDefinition(da.getBType()));
            arr.add(elem);
        }
        for (SclSDO sdo : doType.getSdos()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem()
                .name(sdo.getName())
                .fc(0)
                .type(nullDataDefinition());
            arr.add(elem);
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(SEL_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    private static CmsDataDefinition nullDataDefinition() {
        return new CmsDataDefinition().choice(SEL_BOOLEAN);
    }

    private static CmsDataDefinition bTypeToDataDefinition(String bType) {
        if (bType == null) return nullDataDefinition();
        switch (bType.toUpperCase()) {
            case "BOOLEAN":      return new CmsDataDefinition().choice(SEL_BOOLEAN);
            case "INT8":         return new CmsDataDefinition().choice(SEL_INT8);
            case "INT16":        return new CmsDataDefinition().choice(SEL_INT16);
            case "INT32":        return new CmsDataDefinition().choice(SEL_INT32);
            case "INT64":        return new CmsDataDefinition().choice(SEL_INT64);
            case "INT8U":        return new CmsDataDefinition().choice(SEL_INT8U);
            case "INT16U":       return new CmsDataDefinition().choice(SEL_INT16U);
            case "INT32U":       return new CmsDataDefinition().choice(SEL_INT32U);
            case "INT64U":       return new CmsDataDefinition().choice(SEL_INT64U);
            case "FLOAT32":      return new CmsDataDefinition().choice(SEL_FLOAT32);
            case "FLOAT64":      return new CmsDataDefinition().choice(SEL_FLOAT64);
            case "BIT_STRING":
            case "BITSTRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_BIT_STRING);
                def.alt_bit_string_len.value(0);
                return def;
            }
            case "OCTET_STRING":
            case "OCTETSTRING":
            case "VISSTRING255":
            case "VISIBLE_STRING": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-255);
                return def;
            }
            case "UNICODE_STRING":
            case "UNICODESTRING":
            case "UNICODE255": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_UNICODE_STRING);
                def.alt_unicode_string_len.value(-255);
                return def;
            }
            case "UTC_TIME":
            case "UTCTIME":
            case "TIMESTAMP":    return new CmsDataDefinition().choice(SEL_UTC_TIME);
            case "BINARY_TIME":
            case "BINARYTIME":
            case "ENTRYTIME":    return new CmsDataDefinition().choice(SEL_BINARY_TIME);
            case "QUALITY":      return new CmsDataDefinition().choice(SEL_QUALITY);
            case "DBPOS":        return new CmsDataDefinition().choice(SEL_DBPOS);
            case "TCMD":         return new CmsDataDefinition().choice(SEL_TCMD);
            case "CHECK":        return new CmsDataDefinition().choice(SEL_CHECK);
            case "VISSTRING64": {
                CmsDataDefinition def = new CmsDataDefinition().choice(SEL_VISIBLE_STRING);
                def.alt_visible_string_len.value(-64);
                return def;
            }
            case "STRUCT":       return new CmsDataDefinition().choice(SEL_BOOLEAN);
            default:             return nullDataDefinition();
        }
    }
}
