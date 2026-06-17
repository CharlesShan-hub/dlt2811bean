package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsEnumerated;

/**
 * SmpMod ::= INTEGER (0..2)  —  7.6.7
 * PER: constrained integer, encoded as Int8 (-128..127)
 * sizeof = 4
 */
public class CmsSmpMod extends CmsEnumerated {

    public static final int SAMPLES_PER_NOMINAL_PERIOD = 0;
    public static final int SAMPLES_PER_SECOND         = 1;
    public static final int SECONDS_PER_SAMPLE         = 2;

    public CmsSmpMod() { super(0, 2, SAMPLES_PER_NOMINAL_PERIOD); }
    public CmsSmpMod(int value) { super(0, 2, value); }
}
