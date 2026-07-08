package com.ysh.jcms.utils.scl2.model.ied;

import com.ysh.jcms.utils.scl2.model.instance.SclDOI;
import com.ysh.jcms.utils.scl2.model.input.SclDataSet;
import com.ysh.jcms.utils.scl2.model.control.SclReportControl;
import com.ysh.jcms.utils.scl2.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl2.model.control.SclSampledValueControl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLN extends SclLNBase {

    public SclDOI findDoiByName(String name) {
        for (SclDOI doi : dois()) {
            if (doi.name().equals(name)) {
                return doi;
            }
        }
        return null;
    }

    public SclDataSet findDataSetByName(String name) {
        for (SclDataSet ds : dataSets()) {
            if (ds.name().equals(name)) {
                return ds;
            }
        }
        return null;
    }

    public SclReportControl findReportControlByName(String name) {
        for (SclReportControl rc : reportControls()) {
            if (rc.name().equals(name)) {
                return rc;
            }
        }
        return null;
    }

    public SclGSEControl findGseControlByName(String name) {
        for (SclGSEControl gc : gseControls()) {
            if (gc.name().equals(name)) {
                return gc;
            }
        }
        return null;
    }

    public SclSampledValueControl findSmvControlByName(String name) {
        for (SclSampledValueControl sv : svControls()) {
            if (sv.name().equals(name)) {
                return sv;
            }
        }
        return null;
    }
}
