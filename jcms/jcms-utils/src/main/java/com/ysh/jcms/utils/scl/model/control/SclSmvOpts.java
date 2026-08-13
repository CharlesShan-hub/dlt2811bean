package com.ysh.jcms.utils.scl.model.control;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SampledValueControl 的 {@code <SmvOpts>} 子元素（tSampledValueControl /
 * agSmvOpts）。
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclSmvOpts {

    private Boolean refreshTime;
    private Boolean sampleSynchronized;
    private Boolean sampleRate;
    private Boolean dataSet;
    private Boolean security;
    private Boolean timestamp;
    private Boolean synchSourceId;
}
