package com.ysh.dlt2811bean.scl2.util;

import java.util.ArrayList;
import java.util.List;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.scl2.model.SclDataDefinitionEntry;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclDOType;
import com.ysh.dlt2811bean.scl2.model.SclDA;
import com.ysh.dlt2811bean.scl2.model.SclDO;
import com.ysh.dlt2811bean.scl2.model.SclLNodeType;
import com.ysh.dlt2811bean.scl2.model.SclSDO;
import com.ysh.dlt2811bean.scl2.model.SclServer;

public class SclDataDefinitionResolver {
    
    // -------------------------------------------------------------------------
    // Data definition resolution (for GetDataDefinition service)
    // -------------------------------------------------------------------------

    /**
     * Resolves a data definition for a given reference and optional FC filter.
     * <p>Supports three levels:
     * <ul>
     *   <li>DO level (e.g. "LD0/LLN0.Pos") → returns CDC type + structure of DAs</li>
     *   <li>DA level (e.g. "LD0/LLN0.Pos.stVal") → returns bType-based definition</li>
     *   <li>SDI.BDA level (e.g. "LD0/LLN0.sVC.offset") → returns bType-based definition</li>
     * </ul>
     *
     * @param ref       the data reference
     * @param fc        optional FC filter (may be null or empty)
     * @param templates the data type templates
     * @return the data definition entry, or null if not found
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

        // FC validation
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
            // DA or SDI.BDA level
            String daName = parts[parts.length - 1];
            if (parts.length == 3) {
                String bType = SclDataValueResolver.resolveDaBType(templates, ln, doName, daName);
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

        // DO level
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
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclDA da : doType.getDas()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    da.getName(), da.getFc(), bTypeToDataDefinition(da.getBType())));
        }
        for (SclSDO sdo : doType.getSdos()) {
            entries.add(new CmsDataDefinition.StructureEntry(
                    sdo.getName(), CmsDataDefinition.ofBoolean()));
        }
        return CmsDataDefinition.ofStructure(entries);
    }

    private static CmsDataDefinition bTypeToDataDefinition(String bType) {
        if (bType == null) return CmsDataDefinition.ofBoolean();
        switch (bType.toUpperCase()) {
            case "BOOLEAN": return CmsDataDefinition.ofBoolean();
            case "INT8": return CmsDataDefinition.ofInt8();
            case "INT16": return CmsDataDefinition.ofInt16();
            case "INT32": return CmsDataDefinition.ofInt32();
            case "INT64": return CmsDataDefinition.ofInt64();
            case "INT8U": return CmsDataDefinition.ofInt8U();
            case "INT16U": return CmsDataDefinition.ofInt16U();
            case "INT32U": return CmsDataDefinition.ofInt32U();
            case "INT64U": return CmsDataDefinition.ofInt64U();
            case "FLOAT32": return CmsDataDefinition.ofFloat32();
            case "FLOAT64": return CmsDataDefinition.ofFloat64();
            case "BIT_STRING":
            case "BITSTRING": return CmsDataDefinition.ofBitString(0);
            case "OCTET_STRING":
            case "OCTETSTRING": return CmsDataDefinition.ofOctetString(-255);
            case "VISSTRING255":
            case "VISIBLE_STRING": return CmsDataDefinition.ofVisibleString(-255);
            case "UNICODE_STRING":
            case "UNICODESTRING": return CmsDataDefinition.ofUnicodeString(-255);
            case "UTC_TIME":
            case "UTCTIME": return CmsDataDefinition.ofUtcTime();
            case "BINARY_TIME":
            case "BINARYTIME":
            case "ENTRYTIME": return CmsDataDefinition.ofBinaryTime();
            case "QUALITY": return CmsDataDefinition.ofQuality();
            case "DBPOS": return CmsDataDefinition.ofDbpos();
            case "TCMD": return CmsDataDefinition.ofTcmd();
            case "CHECK": return CmsDataDefinition.ofCheck();
            case "STRUCT": return CmsDataDefinition.ofBoolean();
            case "TIMESTAMP": return CmsDataDefinition.ofUtcTime();
            case "VISSTRING64": return CmsDataDefinition.ofVisibleString(-64);
            case "UNICODE255": return CmsDataDefinition.ofUnicodeString(-255);
            default: return CmsDataDefinition.ofBoolean();
        }
    }
}
