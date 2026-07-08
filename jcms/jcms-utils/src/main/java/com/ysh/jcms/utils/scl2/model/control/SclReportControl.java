package com.ysh.jcms.utils.scl2.model.control;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
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

    // trgOps, optFields, rptEnabled are child elements, keep as String for now
    private String trgOps;
    private String optFields;
    private String rptEnabled;
}
