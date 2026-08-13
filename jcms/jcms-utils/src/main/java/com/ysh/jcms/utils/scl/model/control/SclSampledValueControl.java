package com.ysh.jcms.utils.scl.model.control;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
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
    /** Sampling mode (tSampledValueControl.smpMod, e.g. SmpPerPeriod). */
    private String smpMod;
    /** The {@code <SmvOpts>} child element. */
    private SclSmvOpts smvOpts;
}
