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
    /** 采样模式（tSampledValueControl.smpMod，如 SmpPerPeriod）。 */
    private String smpMod;
    /** {@code <SmvOpts>} 子元素。 */
    private SclSmvOpts smvOpts;
}
