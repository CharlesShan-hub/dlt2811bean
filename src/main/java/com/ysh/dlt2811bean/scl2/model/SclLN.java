package com.ysh.dlt2811bean.scl2.model;

import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclLN {

    private SclLDevice parent;
    private String prefix = "";
    private String lnClass;
    private String inst = "";
    private String desc;
    private String lnType;
    private final List<SclDOI> dois = new ArrayList<>();
    private final List<SclDataSet> dataSets = new ArrayList<>();
    private final List<SclReportControl> reportControls = new ArrayList<>();
    private final List<SclLogControl> logControls = new ArrayList<>();
    private final List<SclGSEControl> gseControls = new ArrayList<>();
    private final List<SclSampledValueControl> svControls = new ArrayList<>();
    private final List<SclInput> inputs = new ArrayList<>();

    public String getFullName() {
        return prefix + lnClass + inst;
    }

    public void addDoi(SclDOI doi) { this.dois.add(doi); }

    public void addDataSet(SclDataSet ds) { this.dataSets.add(ds); }

    public void addReportControl(SclReportControl rc) { this.reportControls.add(rc); }

    public void addLogControl(SclLogControl lc) { this.logControls.add(lc); }

    public void addGseControl(SclGSEControl gc) { this.gseControls.add(gc); }

    public void addSvControl(SclSampledValueControl svc) { this.svControls.add(svc); }

    public void addInput(SclInput input) { this.inputs.add(input); }

    public SclDOI findDoiByName(String name) {
        for (SclDOI doi : dois) {
            if (doi.getName().equals(name)) return doi;
        }
        return null;
    }

    public SclDataSet findDataSetByName(String name) {
        for (SclDataSet ds : dataSets) {
            if (ds.getName().equals(name)) return ds;
        }
        return null;
    }

    public SclReportControl findReportControlByName(String name) {
        for (SclReportControl rc : reportControls) {
            if (rc.getName().equals(name)) return rc;
        }
        return null;
    }

    public SclGSEControl findGseControlByName(String name) {
        for (SclGSEControl gc : gseControls) {
            if (gc.getName().equals(name)) return gc;
        }
        return null;
    }

    public List<String> getDataObjectNames(SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || lnType == null || lnType.isEmpty()) return names;
        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        if (lnt == null) return names;
        for (SclDO doDef : lnt.getDos()) {
            names.add(doDef.getName());
            collectSdoNames(templates, doDef.getType(), doDef.getName(), names);
        }
        return names;
    }

    private void collectSdoNames(SclDataTypeTemplates templates, String doTypeId, String prefix, List<String> names) {
        SclDOType doType = templates.findDoTypeById(doTypeId);
        if (doType == null) return;
        for (SclDA da : doType.getDas()) {
            if ("ST".equals(da.getFc())) {
                names.add(prefix + "." + da.getName());
            }
        }
    }

    public List<String> getDataSetNames() {
        return dataSets.stream().map(SclDataSet::getName).toList();
    }

    public List<String> getReportControlNames(boolean buffered) {
        return reportControls.stream()
            .filter(rc -> Boolean.toString(buffered).equals(rc.getBuffered()))
            .map(SclReportControl::getName)
            .toList();
    }

    public List<String> getLogControlNames() {
        return logControls.stream().map(SclLogControl::getName).toList();
    }

    public List<String> getLogNames() {
        return logControls.stream()
            .map(SclLogControl::getLogName)
            .filter(java.util.Objects::nonNull)
            .filter(name -> !name.isEmpty())
            .toList();
    }

    public List<String> getGseControlNames() {
        return gseControls.stream().map(SclGSEControl::getName).toList();
    }

    public List<String> getSvControlNames() {
        return svControls.stream().map(SclSampledValueControl::getName).toList();
    }

    // -------------------------------------------------------------------------
    // Data Value collection (for GetAllValuesHander service)
    // -------------------------------------------------------------------------

    /**
     * Collects all data values (DAI) under this LN, resolving bType from the data type templates.
     *
     * @param templates the data type templates for type resolution
     * @param fcFilter  optional FC filter, null for no filter, "XX" treated as no filter
     * @param relative  if true, omit the LN prefix from the reference path
     * @return list of data values
     */
    public List<SclDataValue> collectDataValues(SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        List<SclDataValue> result = new ArrayList<>();
        String prefix = relative ? "" : (getFullName() + ".");

        SclLNodeType lnt = null;
        if (templates != null && lnType != null && !lnType.isEmpty()) {
            lnt = templates.findLNodeTypeById(lnType);
        }

        for (SclDOI doi : dois) {
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
        return result;
    }

    private void collectDaiValues(List<SclDAI> dais, String prefix, SclDOType doType,
                                   String fcFilter, List<SclDataValue> result) {
        collectDaiValues(dais, prefix, doType, fcFilter, result, null, null, null);
    }

    private void collectDaiValues(List<SclDAI> dais, String prefix, SclDOType doType,
                                   String fcFilter, List<SclDataValue> result,
                                   SclDataTypeTemplates templates, SclDOType parentDoType, String sdiName) {
        if (dais == null || dais.isEmpty()) return;
        for (SclDAI dai : dais) {
            if (dai.getVal() == null || dai.getVal().isEmpty()) continue;
            String daFc = findDaFc(doType, dai.getName());
            if (fcFilter != null && !fcFilter.equals(daFc)) continue;
            if (fcFilter == null && "SE".equals(daFc)) continue;
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
    public List<SclDataDefinitionEntry> collectDataDefinitions(SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        List<SclDataDefinitionEntry> result = new ArrayList<>();
        String prefix = relative ? "" : (getFullName() + ".");

        List<SclDO> dos = getDosForType(templates, lnType);
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

    private void collectSdoEntries(SclDataTypeTemplates templates, SclDOType parentDoType,
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

    private List<CmsDataDefinition.StructureEntry> buildDaEntries(SclDataTypeTemplates templates,
                                                                   SclDOType doType, String fcFilter) {
        List<CmsDataDefinition.StructureEntry> entries = new ArrayList<>();
        for (SclDA da : doType.getDas()) {
            if (fcFilter != null && !fcFilter.equals(da.getFc())) continue;
            if (!CmsFC.isValid(da.getFc())) continue;
            CmsDataDefinition daDef = resolveBType(templates, da.getBType(), da.getType(), da.getCount());
            if (daDef != null) {
                entries.add(new CmsDataDefinition.StructureEntry(da.getName(), da.getFc(), daDef));
            }
        }
        return entries;
    }

    private CmsDataDefinition resolveBType(SclDataTypeTemplates templates, String bType,
                                            String typeRef, Integer count) {
        if (bType == null) return CmsDataDefinition.ofInt32();
        return switch (bType) {
            case "BOOLEAN" -> CmsDataDefinition.ofBoolean();
            case "INT8" -> CmsDataDefinition.ofInt8();
            case "INT16" -> CmsDataDefinition.ofInt16();
            case "INT32" -> CmsDataDefinition.ofInt32();
            case "INT64" -> CmsDataDefinition.ofInt64();
            case "INT8U" -> CmsDataDefinition.ofInt8U();
            case "INT16U" -> CmsDataDefinition.ofInt16U();
            case "INT32U" -> CmsDataDefinition.ofInt32U();
            case "INT64U" -> CmsDataDefinition.ofInt64U();
            case "FLOAT32" -> CmsDataDefinition.ofFloat32();
            case "FLOAT64" -> CmsDataDefinition.ofFloat64();
            case "BIT STRING" -> CmsDataDefinition.ofBitString(count != null ? count : 0);
            case "OCTET STRING" -> CmsDataDefinition.ofOctetString(count != null ? count : 255);
            case "VisString255", "VISIBLE STRING" -> CmsDataDefinition.ofVisibleString(count != null ? count : 255);
            case "Unicode255", "UNICODE STRING" -> CmsDataDefinition.ofUnicodeString(count != null ? count : 255);
            case "Struct" -> {
                if (templates != null && typeRef != null) {
                    yield resolveStructType(templates, typeRef);
                }
                yield CmsDataDefinition.ofInt32();
            }
            case "Enum" -> CmsDataDefinition.ofInt32U();
            case "Quality" -> CmsDataDefinition.ofQuality();
            case "Timestamp" -> CmsDataDefinition.ofUtcTime();
            case "Check" -> CmsDataDefinition.ofCheck();
            case "Dbpos" -> CmsDataDefinition.ofDbpos();
            case "Tcmd" -> CmsDataDefinition.ofTcmd();
            default -> {
                yield CmsDataDefinition.ofInt32();
            }
        };
    }

    private CmsDataDefinition resolveStructType(SclDataTypeTemplates templates, String typeRef) {
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

    // -------------------------------------------------------------------------
    // Control block value collection (for GetAllCBValues service)
    // -------------------------------------------------------------------------

    /**
     * Collects control block values from this LN based on the given ACSI class.
     * <p>Only LLN0 typically contains control blocks, but the method is on SclLN for consistency.
     *
     * @param acsiClass the ACSI class filter (BRCB, URCB, LCB, GO_CB, MSV_CB, SGCB)
     * @return list of control block entries
     */
    public List<SclCBEntry> collectCBValues(int acsiClass) {
        List<SclCBEntry> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsACSIClass.BRCB:
                for (SclReportControl rc : reportControls) {
                    if (rc.isBuffered()) {
                        result.add(new SclCBEntry(rc.getName(), buildBrcb(rc)));
                    }
                }
                break;
            case CmsACSIClass.URCB:
                for (SclReportControl rc : reportControls) {
                    if (!rc.isBuffered()) {
                        result.add(new SclCBEntry(rc.getName(), buildUrcb(rc)));
                    }
                }
                break;
            case CmsACSIClass.LCB:
                for (SclLogControl lc : logControls) {
                    result.add(new SclCBEntry(lc.getName(), buildLcb(lc)));
                }
                break;
            case CmsACSIClass.GO_CB:
                for (SclGSEControl gse : gseControls) {
                    result.add(new SclCBEntry(gse.getName(), buildGocb(gse)));
                }
                break;
            case CmsACSIClass.MSV_CB:
                for (SclSampledValueControl sv : svControls) {
                    result.add(new SclCBEntry(sv.getName(), buildMsvcb(sv)));
                }
                break;
            case CmsACSIClass.SGCB:
                result.add(new SclCBEntry("SG1", buildSgcb()));
                break;
            default:
                break;
        }
        return result;
    }

    private static CmsCBValue buildBrcb(SclReportControl rc) {
        CmsBRCB brcb = new CmsBRCB();
        brcb.brcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            brcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            brcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            brcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCBValue().selectBrcb();
    }

    private static CmsCBValue buildUrcb(SclReportControl rc) {
        CmsURCB urcb = new CmsURCB();
        urcb.urcbName.set(rc.getName());
        if (rc.getDatSet() != null) {
            urcb.datSet.set(rc.getDatSet());
        }
        if (rc.getRptID() != null) {
            urcb.rptID.set(rc.getRptID());
        }
        if (rc.getConfRev() != null) {
            urcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        return new CmsCBValue().selectUrcb();
    }

    private static CmsCBValue buildLcb(SclLogControl lc) {
        CmsLCB lcb = new CmsLCB();
        lcb.lcbName.set(lc.getName());
        if (lc.getDatSet() != null) {
            lcb.datSet.set(lc.getDatSet());
        }
        if (lc.getLogName() != null) {
            lcb.logRef.set(lc.getLogName());
        }
        return new CmsCBValue().selectLcb();
    }

    private static CmsCBValue buildGocb(SclGSEControl gse) {
        CmsGoCB gocb = new CmsGoCB();
        gocb.goCBName.set(gse.getName());
        if (gse.getDatSet() != null) {
            gocb.datSet.set(gse.getDatSet());
        }
        if (gse.getAppID() != null) {
            gocb.goID.set(gse.getAppID());
        }
        if (gse.getConfRev() != null) {
            gocb.confRev.set(Long.parseLong(gse.getConfRev()));
        }
        return new CmsCBValue().selectGocb();
    }

    private static CmsCBValue buildMsvcb(SclSampledValueControl sv) {
        CmsMSVCB msvcb = new CmsMSVCB();
        msvcb.msvCBName.set(sv.getName());
        if (sv.getDatSet() != null) {
            msvcb.datSet.set(sv.getDatSet());
        }
        if (sv.getSvID() != null) {
            msvcb.msvID.set(sv.getSvID());
        }
        if (sv.getConfRev() != null) {
            msvcb.confRev.set(Long.parseLong(sv.getConfRev()));
        }
        if (sv.getSmpRate() != null && !sv.getSmpRate().isEmpty()) {
            msvcb.smpRate.set(Integer.parseInt(sv.getSmpRate()));
        }
        return new CmsCBValue().selectMsvcb();
    }

    private static CmsCBValue buildSgcb() {
        CmsSGCB sgb = new CmsSGCB();
        sgb.sgcbName.set("SG1");
        return new CmsCBValue().selectSgb();
    }

    // -------------------------------------------------------------------------
    // Data directory collection (for GetDataDirectory service)
    // -------------------------------------------------------------------------

    /**
     * Collects data directory entries at the LN level: lists all DO names.
     * <p>Merges instance DOIs with type template DOs, instance takes priority.
     *
     * @param templates the data type templates for type-based DO listing
     * @return list of directory entries (ref = DO name, fc = null)
     */
    public List<SclDataDirectoryEntry> collectDataDirectory(SclDataTypeTemplates templates) {
        java.util.Set<String> seen = new java.util.HashSet<>();
        List<SclDataDirectoryEntry> entries = new ArrayList<>();

        for (SclDOI doi : dois) {
            String name = doi.getName();
            seen.add(name);
            entries.add(new SclDataDirectoryEntry(name, null));
        }

        if (templates != null && lnType != null && !lnType.isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(lnType);
            if (lnt != null) {
                for (SclDO doDef : lnt.getDos()) {
                    if (!seen.contains(doDef.getName())) {
                        seen.add(doDef.getName());
                        entries.add(new SclDataDirectoryEntry(doDef.getName(), null));
                    }
                }
            }
        }

        return entries;
    }
}
