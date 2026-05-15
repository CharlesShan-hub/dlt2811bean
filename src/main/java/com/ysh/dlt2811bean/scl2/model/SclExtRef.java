package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclExtRef {

    private String desc;
    private String ldInst;
    private String prefix = "";
    private String lnClass;
    private String lnInst = "";
    private String doName;
    private String daName;
    private String iedName;
    private String serviceType;
    private String srcLDInst;
    private String srcPrefix = "";
    private String srcLnClass;
    private String srcLnInst = "";
    private String srcCBName;
}
