package com.ysh.jcms.utils.scl.model.ied;

import com.ysh.jcms.utils.scl.model.input.SclInput;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclLNBase {

    protected SclLDevice parent;
    protected String prefix = "";
    protected String lnClass;
    protected String inst = "";
    protected String desc;
    protected String lnType;
    protected final List<SclDOI> dois = new ArrayList<>();
    protected final List<SclDataSet> dataSets = new ArrayList<>();
    protected final List<SclReportControl> reportControls = new ArrayList<>();
    protected final List<SclLogControl> logControls = new ArrayList<>();
    protected final List<SclGSEControl> gseControls = new ArrayList<>();
    protected final List<SclSampledValueControl> svControls = new ArrayList<>();
    protected final List<SclInput> inputs = new ArrayList<>();

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
