package com.ysh.jcms.datatypes.compound;

public class CmsTimeQuality extends AbstractCmsCompound {

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
        super("TimeQuality");
    }

    public CmsTimeQuality(int tagf, int precision, long fraction) {
        super("TimeQuality");
        this.tagf = tagf;
        this.precision = precision;
        this.fraction = fraction;
    }

    public byte[] encode() {
        return new byte[0];
    }

    public CmsTimeQuality copy() {
        return new CmsTimeQuality(tagf, precision, fraction);
    }
}
