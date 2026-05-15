package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclSampledValueControl {

    private String name;
    private String desc;
    private String svID;
    private String datSet;
    private String confRev;
    private String smpRate;
    private String nofASDU;
    private String multicast;
    private String securityEnable;
}
