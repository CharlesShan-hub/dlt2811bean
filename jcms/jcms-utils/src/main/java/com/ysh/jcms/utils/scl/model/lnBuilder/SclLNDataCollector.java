package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.model.control.SclSGCBState;
import com.ysh.jcms.utils.scl.model.data.SclDataDefinitionEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLNBase;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SclLNDataCollector {

    public static List<SclDataValue> collectDataValues(SclLNBase ln, SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        return collectDataValues(ln, templates, fcFilter, relative, null);
    }

    public static List<SclDataValue> collectDataValues(SclLNBase ln, SclDataTypeTemplates templates, String fcFilter, boolean relative, CmsServerSession session) {
        List<SclDataValue> result = new ArrayList<>();
        String prefix = relative ? "" : (ln.getFullName() + ".");

        SclLNodeType lnt = null;
        if (templates != null && ln.getLnType() != null && !ln.getLnType().isEmpty()) {
            lnt = templates.findLNodeTypeById(ln.getLnType());
        }

        for (SclDOI doi : ln.getDois()) {
            String doiPrefix = prefix + doi.getName();
            SclDOType doType = resolveDoType(templates, lnt, doi.getName());

            collectDaiValues(doi.getDais(), doiPrefix, doType, fcFilter, result);
            for (SclSDI sdi : doi.getSdis()) {
                String sdiPrefix = doiPrefix + "." + sdi.getName();
                SclDOType sdiDoType = resolveSdiDoType(doType, sdi.getName(), templates);
                collectDaiValues(sdi.getDais(), sdiPrefix, sdiDoType, fcFilter, result,
                        templates, doType, sdi.getName());
            }
        }

        // collect dynamic SG1~SGn data from session state
        if (session != null) {
            CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
            if (!setting.isSgDefaultEnabled()) return result;
            String sgcbRef = ln.getFullName() + "." + setting.getSgDefaultName();
            Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(session);
            SclSGCBState state = sgcbStates.get(sgcbRef);
            if (state != null) {
                for (int sgNum = 1; sgNum <= state.getNumOfSG(); sgNum++) {
                    Map<String, String> sgData = state.getSgValues(sgNum);
                    if (sgData != null && !sgData.isEmpty()) {
                        String sgPrefix = prefix + "SG" + sgNum;
                        for (Map.Entry<String, String> entry : sgData.entrySet()) {
                            String ref = sgPrefix + "." + entry.getKey();
                            result.add(new SclDataValue(ref, entry.getValue(), "INT32"));
                        }
                    }
                }
            }
        }

        return result;
    }

    private static boolean matchesSgFc(String fcFilter, String daFc) {
        if (fcFilter == null) return !"SE".equals(daFc);
        if (fcFilter.equals(daFc)) return true;
        if ("SG".equals(fcFilter)) {
            CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
            return setting.isSgDefaultEnabled() && ("CF".equals(daFc) || "DC".equals(daFc) || "SE".equals(daFc));
        }
        return false;
    }

    private static void collectDaiValues(List<SclDAI> dais, String prefix, SclDOType doType,
                                         String fcFilter, List<SclDataValue> result) {
        collectDaiValues(dais, prefix, doType, fcFilter, result, null, null, null);
    }

    private static void collectDaiValues(List<SclDAI> dais, String prefix, SclDOType doType,
                                   String fcFilter, List<SclDataValue> result,
                                   SclDataTypeTemplates templates, SclDOType parentDoType, String sdiName) {
        if (dais == null || dais.isEmpty()) return;
        for (SclDAI dai : dais) {
            if (dai.getVal() == null || dai.getVal().isEmpty()) continue;
            String daFc = findDaFc(doType, dai.getName());
            if (!matchesSgFc(fcFilter, daFc)) continue;
            String ref = prefix + "." + dai.getName();
            String bType = findDaBType(doType, dai.getName());
            if (bType == null && templates != null && parentDoType != null && sdiName != null) {
                bType = findBdaBType(templates, parentDoType, sdiName, dai.getName());
            }
            result.add(new SclDataValue(ref, dai.getVal(), bType));
        }
    }

    private static String findDaBType(SclDOType doType, String daName) {
        if (doType == null) return null;
        SclDA da = doType.findDaByName(daName);
        return da != null ? da.getBType() : null;
    }

    private static String findBdaBType(SclDataTypeTemplates templates, SclDOType parentDoType, String sdiName, String bdaName) {
        if (templates == null || parentDoType == null) return null;
        for (SclDA da : parentDoType.getDas()) {
            if (da.getName().equals(sdiName) && "Struct".equals(da.getBType()) && da.getType() != null) {
                SclDAType dat = templates.findDaTypeById(da.getType());
                if (dat != null) {
                    SclBDA bda = dat.findBdaByName(bdaName);
                    if (bda != null) return bda.getBType();
                }
            }
        }
        return null;
    }

    private static String findDaFc(SclDOType doType, String daName) {
        if (doType == null) return null;
        SclDA da = doType.findDaByName(daName);
        return da != null ? da.getFc() : null;
    }

    private static SclDOType resolveDoType(SclDataTypeTemplates templates, SclLNodeType lnt, String doName) {
        if (templates == null || lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef != null && doDef.getType() != null) {
            return templates.findDoTypeById(doDef.getType());
        }
        return null;
    }

    private static SclDOType resolveSdiDoType(SclDOType doType, String sdiName, SclDataTypeTemplates templates) {
        if (doType == null || templates == null) return null;
        for (SclDA da : doType.getDas()) {
            if (da.getName().equals(sdiName) && "Struct".equals(da.getBType()) && da.getType() != null) {
                return templates.findDoTypeById(da.getType());
            }
        }
        for (SclSDO sdo : doType.getSdos()) {
            if (sdo.getName().equals(sdiName) && sdo.getType() != null) {
                return templates.findDoTypeById(sdo.getType());
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Data definition collection (for GetAllDataDefinition service)
    // -------------------------------------------------------------------------

    /**
     * Collects all data definitions (DO type definitions) under this LN.
     *
     * @param templates the data type templates for type resolution
     * @param fcFilter  optional FC filter, null for no filter
     * @param relative  if true, omit the LN prefix from the reference path
     * @return list of data definition entries
     */
    public static List<SclDataDefinitionEntry> collectDataDefinitions(SclLNBase ln, SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        List<SclDataDefinitionEntry> result = new ArrayList<>();
        String prefix = relative ? "" : (ln.getFullName() + ".");

        List<SclDO> dos = getDosForType(templates, ln.getLnType());
        if (dos == null || dos.isEmpty()) return result;

        for (SclDO doDef : dos) {
            if (templates == null || doDef.getType() == null) continue;
            SclDOType doType = templates.findDoTypeById(doDef.getType());
            if (doType == null) continue;

            List<CmsDataDefinition.StructureEntry> daEntries = buildDaEntries(templates, doType, fcFilter);
            if (daEntries.isEmpty()) continue;

            String ref = prefix + doDef.getName();
            CmsDataDefinition def = CmsDataDefinition.ofStructure(daEntries);
            result.add(new SclDataDefinitionEntry(ref, doType.getCdc(), def));

            collectSdoEntries(templates, doType, prefix + doDef.getName() + ".", fcFilter, result);
        }
        return result;
    }

    private static void collectSdoEntries(SclDataTypeTemplates templates, SclDOType parentDoType,
                                    String parentPrefix, String fcFilter, List<SclDataDefinitionEntry> result) {
        if (templates == null) return;
        for (SclSDO sdo : parentDoType.getSdos()) {
            if (sdo.getType() == null) continue;
            SclDOType sdoDoType = templates.findDoTypeById(sdo.getType());
            if (sdoDoType == null) continue;

            List<CmsDataDefinition.StructureEntry> daEntries = buildDaEntries(templates, sdoDoType, fcFilter);
            if (daEntries.isEmpty()) continue;

            String ref = parentPrefix + sdo.getName();
            CmsDataDefinition def = CmsDataDefinition.ofStructure(daEntries);
            result.add(new SclDataDefinitionEntry(ref, sdoDoType.getCdc(), def));
        }
    }

    private static List<CmsDataDefinition.StructureEntry> buildDaEntries(SclDataTypeTemplates templates,
                                                                   SclDOType doType, String fcFilter) {
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclDA da : doType.getDas()) {
            if (!matchesSgFc(fcFilter, da.getFc())) continue;
            if (!CmsFC.isValid(da.getFc())) continue;
            CmsDataDefinition daDef = resolveBType(templates, da.getBType(), da.getType(), da.getCount());
            if (daDef != null) {
                entries.add(new CmsDataDefinition.StructureEntry(da.getName(), da.getFc(), daDef));
            }
        }
        return entries;
    }

    private static CmsDataDefinition resolveBType(SclDataTypeTemplates templates, String bType,
                                            String typeRef, Integer count) {
        if (bType == null) return CmsDataDefinition.ofInt32();
        switch (bType) {
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
            case "BIT STRING": return CmsDataDefinition.ofBitString(count != null ? count : 0);
            case "OCTET STRING": return CmsDataDefinition.ofOctetString(count != null ? count : 255);
            case "VisString255":
            case "VISIBLE STRING": return CmsDataDefinition.ofVisibleString(count != null ? count : 255);
            case "Unicode255":
            case "UNICODE STRING": return CmsDataDefinition.ofUnicodeString(count != null ? count : 255);
            case "Struct": {
                if (templates != null && typeRef != null) {
                    return resolveStructType(templates, typeRef);
                }
                return CmsDataDefinition.ofInt32();
            }
            case "Enum": return CmsDataDefinition.ofInt32U();
            case "Quality": return CmsDataDefinition.ofQuality();
            case "Timestamp": return CmsDataDefinition.ofUtcTime();
            case "Check": return CmsDataDefinition.ofCheck();
            case "Dbpos": return CmsDataDefinition.ofDbpos();
            case "Tcmd": return CmsDataDefinition.ofTcmd();
            default: return CmsDataDefinition.ofInt32();
        }
    }

    private static CmsDataDefinition resolveStructType(SclDataTypeTemplates templates, String typeRef) {
        SclDAType daType = templates.findDaTypeById(typeRef);
        if (daType == null) return CmsDataDefinition.ofInt32();
        List<CmsDataDefinition.StructureEntry> bdaEntries = new ArrayList<>();
        for (SclBDA bda : daType.getBdas()) {
            CmsDataDefinition bdaDef = resolveBType(templates, bda.getBType(), bda.getType(), bda.getCount());
            if (bdaDef != null) {
                bdaEntries.add(new CmsDataDefinition.StructureEntry(bda.getName(), bdaDef));
            }
        }
        return CmsDataDefinition.ofStructure(bdaEntries);
    }

    private static List<SclDO> getDosForType(SclDataTypeTemplates templates, String lnType) {
        if (templates == null || lnType == null || lnType.isEmpty()) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        return lnt != null ? lnt.getDos() : null;
    }
}
