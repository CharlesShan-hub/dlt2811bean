package com.ysh.jcms.datatypes.enumerated;

public class CmsOrCat extends AbstractCmsEnumerated {

    public static final int NOT_SUPPORTED     = 0;
    public static final int BAY_CONTROL       = 1;
    public static final int STATION_CONTROL   = 2;
    public static final int REMOTE_CONTROL    = 3;
    public static final int AUTOMATIC_BAY     = 4;
    public static final int AUTOMATIC_STATION = 5;
    public static final int AUTOMATIC_REMOTE  = 6;
    public static final int MAINTENANCE       = 7;
    public static final int PROCESS           = 8;

    public CmsOrCat() {
        this(0);
    }

    public CmsOrCat(int value) {
        super("OrCat", value, 9);
    }

    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("should encode with CmsOriginator");
    }

    public static CmsOrCat decode(byte[] data) {
        throw new UnsupportedOperationException("should decode with CmsOriginator");
    }

    @Override
    public CmsOrCat copy() {
        CmsOrCat clone = new CmsOrCat();
        return copyTo(clone);
    }
}
