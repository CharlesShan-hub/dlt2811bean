package com.ysh.jcms.datatypes.code;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsQuality extends AbstractCmsCodedEnum<CmsQuality> {

    public static final int GOOD              = 0;
    public static final int INVALID           = 1;
    public static final int RESERVED_VALIDITY = 2;
    public static final int QUESTIONABLE      = 3;

    public static final int VALIDITY      = 0;
    public static final int VALIDITY_WIDTH = 2;
    public static final int OVERFLOW     = 2;
    public static final int OUT_OF_RANGE = 3;
    public static final int BAD_REFERENCE = 4;
    public static final int OSCILLATORY  = 5;
    public static final int FAILURE      = 6;
    public static final int OLD_DATA     = 7;
    public static final int INCONSISTENT = 8;
    public static final int INACCURATE   = 9;
    public static final int SOURCE       = 10;
    public static final int TEST         = 11;
    public static final int OPERATOR_BLOCKED = 12;

    public CmsQuality() {
        this(0L);
    }

    public CmsQuality(long value) {
        super("Quality", value, 13);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_quality_encode(toPerBytes(), buf, outLen);
    }

    public static CmsQuality decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_quality_decode(data, data.length, val);
        return new CmsQuality(fromPerBytes(val, 13));
    }
}
