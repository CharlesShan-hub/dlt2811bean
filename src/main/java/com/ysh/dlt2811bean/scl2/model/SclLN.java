package com.ysh.dlt2811bean.scl2.model;

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
}
