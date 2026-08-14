package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.choice.CmsCbValueChoice;
import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.sequence.block.CmsSgcb;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.core.data.sequence.common.CmsDataDefinitionStructElem;
import com.ysh.jcms.core.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.core.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.core.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.CbConverter;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataDefinitionResolver;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.state.CbStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * "Read all" values service —— all data values (8.3.4) / all data definitions
 * (8.3.5) / all control block values (8.3.6).
 * <p>
 * Each method returns the complete result list; paging (referenceAfter /
 * pageSize) is handled by the handler layer. Control block values (8.3.6)
 * overlay runtime state, aligned with the GetXxxCBValues behavior.
 */
public final class SclAllValuesService {

    private SclAllValuesService() {
    }

    // ==================== All data values (8.3.4) ====================

    /**
     * Gets all data values (expanded to DA level).
     * <p>
     * Per standard 8.3.4, returns the values of all DAs under the given LN
     * (excluding the functional constraint SE), instead of a single value at DO
     * level.
     *
     * @param doc
     *            SCL document
     * @param ied
     *            the currently associated IED
     * @param lns
     *            list of resolved LNs
     * @param fc
     *            FC filter code (null or empty means no filter)
     * @return list of data value entries
     */
    public static List<CmsDataValueEntry> getAllDataValues(SclDocument doc, SclIED ied, List<SclLN> lns, String fc) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        List<CmsDataValueEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            String ldInst = Navigator.findLdInst(ied, ln);
            if (ldInst == null || templates == null)
                continue;
            String base = ied.name() + "/" + ldInst + "/" + ln.getFullName() + ".";
            collectDoDaEntries(doc, ln, templates, base, fc, entries);
        }
        return entries;
    }

    /** Expands the DAs of all DOs under the LN, handling SDOs recursively. */
    private static void collectDoDaEntries(SclDocument doc, SclLN ln, SclDataTypeTemplates templates, String base, String fc,
            List<CmsDataValueEntry> entries) {
        if (ln.lnType() == null || ln.lnType().isEmpty())
            return;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return;
        for (SclDO doDef : lnt.dos()) {
            String doTypeId = doDef.type();
            if (doTypeId == null || doTypeId.isEmpty())
                continue;
            SclDOType doType = templates.findDoTypeById(doTypeId);
            if (doType == null)
                continue;
            String doPrefix = base + doDef.name();
            collectDaEntries(doc, ln, doPrefix, doType, fc, entries, templates);
        }
    }

    /** Collects the values of all DAs under a DO (including SDO recursion). */
    private static void collectDaEntries(SclDocument doc, SclLN ln, String doPrefix, SclDOType doType, String fc,
            List<CmsDataValueEntry> entries, SclDataTypeTemplates templates) {
        // Ordinary DA
        for (SclDA da : doType.das()) {
            if (fc != null && !fc.isEmpty() && !"XX".equalsIgnoreCase(fc)) {
                if (!fc.equalsIgnoreCase(da.fc()))
                    continue;
            }
            String fullRef = doPrefix + "." + da.name();
            DataValueEntry dv = DataValueResolver.resolve(doc, fullRef);
            if (dv != null && dv.val() != null && !dv.val().isEmpty() && dv.bType() != null && !dv.bType().isEmpty()) {
                entries.add(new CmsDataValueEntry().reference(fullRef).value(DataConverter.toCmsData(dv)));
            }
        }
        // SDO recursion
        for (SclSDO sdo : doType.sdos()) {
            String sdoTypeId = sdo.type();
            if (sdoTypeId == null || sdoTypeId.isEmpty())
                continue;
            SclDOType sdoDoType = templates.findDoTypeById(sdoTypeId);
            if (sdoDoType != null) {
                collectDaEntries(doc, ln, doPrefix + "." + sdo.name(), sdoDoType, fc, entries, templates);
            }
        }
    }

    // ==================== All data definitions (8.3.5) ====================

    /**
     * Gets all data definitions.
     *
     * @param doc
     *            SCL document
     * @param lns
     *            list of resolved LNs
     * @param fc
     *            FC filter code (null or empty means no filter)
     * @return list of data definition entries
     */
    public static List<CmsDataDefinitionEntry> getAllDataDefinition(SclDocument doc, List<SclLN> lns, String fc) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        List<CmsDataDefinitionEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            if (templates == null || ln.lnType() == null)
                continue;
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt == null)
                continue;

            for (SclDO doDef : lnt.dos()) {
                // FC filter
                if (fc != null) {
                    SclDOType doType = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                    if (doType == null)
                        continue;
                    boolean hasFc = false;
                    for (SclDA da : doType.das()) {
                        if (fc.equalsIgnoreCase(da.fc())) {
                            hasFc = true;
                            break;
                        }
                    }
                    if (!hasFc)
                        continue;
                }

                CmsDataDefinition def = buildDoDefinition(templates, doDef);
                if (def == null)
                    continue;

                SclDOType doType2 = doDef.type() != null ? templates.findDoTypeById(doDef.type()) : null;
                String cdc = doType2 != null ? doType2.cdc() : null;

                CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry().reference(doDef.name());
                if (cdc != null)
                    entry.cdcType(cdc);
                entry.definition = def;
                entries.add(entry);
            }
        }
        return entries;
    }

    private static CmsDataDefinition buildDoDefinition(SclDataTypeTemplates templates, SclDO doDef) {
        if (doDef.type() == null)
            return null;
        SclDOType doType = templates.findDoTypeById(doDef.type());
        if (doType == null)
            return null;

        List<CmsDataDefinitionStructElem> arr = new ArrayList<>();
        for (SclDA da : doType.das()) {
            String bType = da.bType();
            if (bType == null)
                bType = "BOOLEAN";
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem().name(da.name())
                    .fc(da.fc() != null ? CmsFC.fromCodeOr(da.fc(), CmsFC.XX) : 0).type(DataDefinitionResolver.toDataDefinition(bType));
            arr.add(elem);
        }
        for (SclSDO sdo : doType.sdos()) {
            CmsDataDefinitionStructElem elem = new CmsDataDefinitionStructElem().name(sdo.name());
            elem.type(DataDefinitionResolver.toDataDefinition(null));
            arr.add(elem);
        }

        CmsDataDefinition def = new CmsDataDefinition();
        def.choice(CmsDataDefinition.CHOICE_STRUCTURE);
        def.alt_structure = arr;
        return def;
    }

    // ==================== All control block values (8.3.6) ====================

    /**
     * Gets all control block values.
     *
     * @param lns
     *            list of resolved LNs
     * @param acsiClass
     *            ACSI class (BRCB/URCB/LCB/GOCB/MSVCB)
     * @return list of control block value entries
     */
    public static List<CmsCbValueEntry> getAllCbValues(List<SclLN> lns, int acsiClass) {
        List<CmsCbValueEntry> entries = new ArrayList<>();
        for (SclLN ln : lns) {
            List<CbPair> cbPairs = collectCbValues(ln, acsiClass);
            for (CbPair cb : cbPairs) {
                entries.add(new CmsCbValueEntry().reference(ln.getFullName() + "." + cb.ref).value(cb.value));
            }
        }
        return entries;
    }

    private static List<CbPair> collectCbValues(SclLN ln, int acsiClass) {
        List<CbPair> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsAcsiClass.BRCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if ("true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), overlayBrcb(ln, rc.name(), CbConverter.brcbFrom(rc))));
                    }
                }
                break;
            case CmsAcsiClass.URCB :
                for (SclReportControl rc : ln.reportControls()) {
                    if (!"true".equals(rc.buffered())) {
                        result.add(new CbPair(rc.name(), overlayUrcb(ln, rc.name(), CbConverter.urcbFrom(rc))));
                    }
                }
                break;
            case CmsAcsiClass.LCB :
                for (SclLogControl lc : ln.logControls()) {
                    result.add(new CbPair(lc.name(), overlayLcb(ln, lc.name(), CbConverter.lcbFrom(lc))));
                }
                break;
            case CmsAcsiClass.GOCB :
                for (SclGSEControl gc : ln.gseControls()) {
                    result.add(new CbPair(gc.name(), overlayGocb(ln, gc.name(), CbConverter.gocbFrom(gc))));
                }
                break;
            case CmsAcsiClass.MSVCB :
                for (SclSampledValueControl sv : ln.svControls()) {
                    result.add(new CbPair(sv.name(), overlayMsvcb(ln, sv.name(), CbConverter.msvcbFrom(sv))));
                }
                break;
            case CmsAcsiClass.SGCB :
                for (CbPair pair : collectSgcb(ln)) {
                    result.add(pair);
                }
                break;
            default :
                break;
        }
        return result;
    }

    // ==================== Runtime state overlay (8.3.6, aligned with the
    // GetXxxCBValues behavior)
    // ====================

    /**
     * Full control block reference: {@code ldInst/lnName.cbName} (consistent with
     * the reference format used by the Set service).
     */
    private static String fullRef(SclLN ln, String cbName) {
        SclLDevice ld = ln.parentLd();
        if (ld == null || ld.inst() == null) {
            return null;
        }
        return ld.inst() + "/" + ln.getFullName() + "." + cbName;
    }

    private static CmsCbValueChoice overlayBrcb(SclLN ln, String cbName, CmsCbValueChoice choice) {
        String ref = fullRef(ln, cbName);
        if (ref == null) {
            return choice;
        }
        CmsBrcb rt = CbStateManager.RCB.get(ref);
        if (rt != null) {
            SclControlBlockService.applyRuntimeState(choice.altBrcb, rt);
        }
        return choice;
    }

    private static CmsCbValueChoice overlayUrcb(SclLN ln, String cbName, CmsCbValueChoice choice) {
        String ref = fullRef(ln, cbName);
        if (ref == null) {
            return choice;
        }
        CmsBrcb rt = CbStateManager.RCB.get(ref);
        if (rt != null) {
            SclControlBlockService.overlayUrcbRuntime(choice.altUrcb, rt);
        }
        return choice;
    }

    private static CmsCbValueChoice overlayLcb(SclLN ln, String cbName, CmsCbValueChoice choice) {
        String ref = fullRef(ln, cbName);
        if (ref == null) {
            return choice;
        }
        CmsLcb rt = CbStateManager.LCB.get(ref);
        if (rt != null) {
            choice.altLcb = rt;
        }
        return choice;
    }

    private static CmsCbValueChoice overlayGocb(SclLN ln, String cbName, CmsCbValueChoice choice) {
        String ref = fullRef(ln, cbName);
        if (ref == null) {
            return choice;
        }
        CmsGoCb rt = CbStateManager.GOCB.get(ref);
        if (rt != null) {
            choice.altGocb = rt;
        }
        return choice;
    }

    private static CmsCbValueChoice overlayMsvcb(SclLN ln, String cbName, CmsCbValueChoice choice) {
        String ref = fullRef(ln, cbName);
        if (ref == null) {
            return choice;
        }
        CmsMsvcb rt = CbStateManager.MSVCB.get(ref);
        if (rt != null) {
            choice.altMsvcb = rt;
        }
        return choice;
    }

    // ==================== SGCB (Setting Group Control Block) ====================

    private static List<CbPair> collectSgcb(SclLN ln) {
        List<CbPair> result = new ArrayList<>();
        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
        for (int i = 1; i <= numOfSG; i++) {
            String sgName = "SG" + i;
            CmsCbValueChoice choice = overlaySgcb(ln, sgName, null);
            result.add(new CbPair(sgName, choice));
        }
        return result;
    }

    private static CmsCbValueChoice overlaySgcb(SclLN ln, String sgName, CmsCbValueChoice unused) {
        String ref = fullRef(ln, sgName);
        if (ref == null) {
            ref = ln.getFullName() + "." + sgName;
        }
        CmsSgcb rt = CbStateManager.SGCB.get(ref);
        if (rt != null) {
            return new CmsCbValueChoice().altSgcb(rt);
        }
        // Fallback: create a default SGCB entry
        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
        CmsSgcb sgcb = new CmsSgcb().numOfSG(numOfSG).actSG(1).editSG(1);
        sgcb.tActEdt.now();
        sgcb.setPresent("resvTms", false);
        return new CmsCbValueChoice().altSgcb(sgcb);
    }

    // ==================== Internal data structure ====================

    private static final class CbPair {
        final String ref;
        final CmsCbValueChoice value;

        CbPair(String ref, CmsCbValueChoice value) {
            this.ref = ref;
            this.value = value;
        }
    }
}
