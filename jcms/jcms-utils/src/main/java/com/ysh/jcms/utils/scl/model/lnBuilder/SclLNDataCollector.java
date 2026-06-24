package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.utils.scl.model.data.SclDataDefinitionEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLNBase;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.*;

import java.util.ArrayList;
import java.util.List;

public class SclLNDataCollector {

    public static List<SclDataValue> collectDataValues(SclLNBase ln, SclDataTypeTemplates templates, String fcFilter, boolean relative) {

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
                collectDaiValues(sdi.getDais(), sdiPrefix, null, fcFilter, result,
                        templates, doType, sdi.getName());
            }
        }

        return result;
    }

    private static boolean matchesSgFc(String fcFilter, String daFc) {
        if (fcFilter == null) return !"SE".equals(daFc);
        if (fcFilter.equals(daFc)) return true;
        if ("SG".equals(fcFilter)) {
            return "CF".equals(daFc) || "DC".equals(daFc) || "SE".equals(daFc);
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
        if (doDef == null || doDef.getType() == null) return null;
        return templates.findDoTypeById(doDef.getType());
    }

    // ========================================================================
    // Data Definition collection (for GetAllDataDefinition service)
    // ========================================================================

    public static List<SclDataDefinitionEntry> collectDataDefinitions(SclLNBase ln, SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        List<SclDataDefinitionEntry> entries = new ArrayList<>();
        String prefix = relative ? "" : (ln.getFullName() + ".");

        SclLNodeType lnt = null;
        if (templates != null && ln.getLnType() != null && !ln.getLnType().isEmpty()) {
            lnt = templates.findLNodeTypeById(ln.getLnType());
        }

        for (SclDOI doi : ln.getDois()) {
            String doiPrefix = prefix + doi.getName();
            SclDOType doType = resolveDoType(templates, lnt, doi.getName());

            collectDaiDefinitions(doi.getDais(), doiPrefix, doType, fcFilter, entries);
            for (SclSDI sdi : doi.getSdis()) {
                String sdiPrefix = doiPrefix + "." + sdi.getName();
                collectDaiDefinitions(sdi.getDais(), sdiPrefix, null, fcFilter, entries,
                        templates, doType, sdi.getName());
            }
        }

        return entries;
    }

    private static void collectDaiDefinitions(List<SclDAI> dais, String prefix, SclDOType doType,
                                              String fcFilter, List<SclDataDefinitionEntry> entries) {
        collectDaiDefinitions(dais, prefix, doType, fcFilter, entries, null, null, null);
    }

    private static void collectDaiDefinitions(List<SclDAI> dais, String prefix, SclDOType doType,
                                        String fcFilter, List<SclDataDefinitionEntry> entries,
                                        SclDataTypeTemplates templates, SclDOType parentDoType, String sdiName) {
        if (dais == null || dais.isEmpty()) return;
        for (SclDAI dai : dais) {
            String daFc = findDaFc(doType, dai.getName());
            if (!matchesSgFc(fcFilter, daFc)) continue;
            String ref = prefix + "." + dai.getName();
            String bType = findDaBType(doType, dai.getName());
            if (bType == null && templates != null && parentDoType != null && sdiName != null) {
                bType = findBdaBType(templates, parentDoType, sdiName, dai.getName());
            }
            entries.add(new SclDataDefinitionEntry(ref, bType, null));
        }
    }
}
