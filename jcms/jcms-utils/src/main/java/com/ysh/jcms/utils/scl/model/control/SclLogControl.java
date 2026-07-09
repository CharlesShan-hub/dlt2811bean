package com.ysh.jcms.utils.scl.model.control;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLogControl {

    private String name;
    private String desc;
    private String logName;
    private String datSet;
    private String confRev;
    private String intgPd;
    private String logEna;
    private String reasonCode;
    private String logEnabled;

    // trgOps and optFields are child elements in XML, keep as String for now
    private String trgOps;
    private String optFields;
}
