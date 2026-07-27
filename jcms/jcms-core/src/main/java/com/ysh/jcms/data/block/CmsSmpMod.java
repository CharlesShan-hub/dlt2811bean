package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsEnum;
import com.ysh.jcms.data.InnerSmpMod;

/**
 * SmpMod ::= INTEGER { samples-per-nominal-period(0), samples-per-second(1),
 * seconds-per-sample(2) } (0..2) — 7.6.7
 */
@CmsEnum.ValueRange(min = 0, max = 2)
public class CmsSmpMod extends CmsEnum<CmsSmpMod> {

    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    public static final int SAMPLES_PER_SECOND = 1;
    public static final int SECONDS_PER_SAMPLE = 2;

    public CmsSmpMod() {
        super(new InnerSmpMod());
    }
    public CmsSmpMod(int value) {
        this();
        this.value(value);
    }
}
