package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclReportControl {

    private String name;
    private String desc;
    private String rptID;
    private String datSet;
    private String confRev;
    private String buffered;
    private String bufTime;
    private String intgPd;
    private String indexed;
    private String trgOps;
    private String optFields;
    private String rptEnabled;

    public boolean isBuffered() {
        return "true".equalsIgnoreCase(buffered) || "1".equals(buffered);
    }
}
