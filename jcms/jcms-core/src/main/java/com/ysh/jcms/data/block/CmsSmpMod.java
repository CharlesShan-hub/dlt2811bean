package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerSmpMod;

/**
 * SmpMod ::= INTEGER { samples-per-nominal-period(0), samples-per-second(1),
 * seconds-per-sample(2) } (0..2) — 7.6.7
 */
public class CmsSmpMod extends CmsType {

    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    public static final int SAMPLES_PER_SECOND = 1;
    public static final int SECONDS_PER_SAMPLE = 2;

    public CmsSmpMod() {
        super(new InnerSmpMod());
    }
    public CmsSmpMod(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerSmpMod) inner).value;
    }
    public CmsSmpMod value(int v) {
        if (v < 0 || v > 2)
            throw new IllegalArgumentException("SmpMod out of range [0,2]: " + v);
        ((InnerSmpMod) inner).value = v;
        return this;
    }
}
