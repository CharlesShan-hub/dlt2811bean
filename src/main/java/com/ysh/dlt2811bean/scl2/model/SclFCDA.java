package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclFCDA {

    private String ldInst;
    private String prefix = "";
    private String lnClass;
    private String lnInst = "";
    private String doName;
    private String daName;
    private String fc;
}
