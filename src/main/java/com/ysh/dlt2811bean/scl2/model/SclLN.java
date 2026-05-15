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
}
