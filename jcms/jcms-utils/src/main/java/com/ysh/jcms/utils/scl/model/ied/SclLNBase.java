package com.ysh.jcms.utils.scl.model.ied;

import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclInput;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLNBase {

    private String prefix;
    private String lnClass;
    private String inst;
    private String desc;
    private String lnType;
    /** IED 类型标识（厂商扩展属性，国际 schema 的 tLN 无此属性，国网 SCD 常见）。 */
    private String iedType;

    /** 所属逻辑设备（解析时由 SclLDevice.addLn 建立，用于推导完整控制块引用）。 */
    private transient SclLDevice parentLd;

    private final List<SclDOI> dois = new ArrayList<>();
    private final List<SclDataSet> dataSets = new ArrayList<>();
    private final List<SclReportControl> reportControls = new ArrayList<>();
    private final List<SclLogControl> logControls = new ArrayList<>();
    private final List<SclGSEControl> gseControls = new ArrayList<>();
    private final List<SclSampledValueControl> svControls = new ArrayList<>();
    private final List<SclInput> inputs = new ArrayList<>();

    public SclLNBase addDoi(SclDOI doi) {
        this.dois.add(doi);
        return this;
    }

    public SclLNBase addDataSet(SclDataSet dataSet) {
        this.dataSets.add(dataSet);
        return this;
    }

    public SclLNBase addReportControl(SclReportControl reportControl) {
        this.reportControls.add(reportControl);
        return this;
    }

    public SclLNBase addLogControl(SclLogControl logControl) {
        this.logControls.add(logControl);
        return this;
    }

    public SclLNBase addGseControl(SclGSEControl gseControl) {
        this.gseControls.add(gseControl);
        return this;
    }

    public SclLNBase addSvControl(SclSampledValueControl svControl) {
        this.svControls.add(svControl);
        return this;
    }

    public SclLNBase addInput(SclInput input) {
        this.inputs.add(input);
        return this;
    }

    public String getFullName() {
        String p = prefix != null ? prefix : "";
        return p + lnClass + (inst != null ? inst : "");
    }
}
