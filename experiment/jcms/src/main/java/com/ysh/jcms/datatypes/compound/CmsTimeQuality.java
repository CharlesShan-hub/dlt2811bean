package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;

public class CmsTimeQuality extends Structure {

    public static final int LEAP_SECOND_KNOWN = 0;
    public static final int CLOCK_FAILURE     = 1;
    public static final int CLOCK_NOT_SYNCED  = 2;

    public static final int TIME_PRECISION_0_BIT  = 0;
    public static final int TIME_PRECISION_1_BIT  = 1;
    public static final int TIME_PRECISION_2_BIT  = 2;
    public static final int TIME_PRECISION_3_BIT  = 3;
    public static final int TIME_PRECISION_ILLEGAL_START = 25;
    public static final int TIME_PRECISION_ILLEGAL_END   = 30;
    public static final int TIME_PRECISION_NOT_SPECIFIED = 31;

    public int tagf;
    public int precision;
    public long fraction;

    public CmsTimeQuality() {
    }

    public static class ByReference extends CmsTimeQuality implements Structure.ByReference {
    }

    @Override
    protected java.util.List<String> getFieldOrder() {
        return java.util.Arrays.asList("tagf", "precision", "fraction");
    }
}
