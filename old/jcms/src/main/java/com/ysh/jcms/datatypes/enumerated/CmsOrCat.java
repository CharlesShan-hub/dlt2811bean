package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsOrCat extends AbstractCmsEnumerated<CmsOrCat> {

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
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        throw new UnsupportedOperationException("should encode with CmsOriginator");
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 8);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        throw new UnsupportedOperationException("CmsOrCat has no FFI decode");
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        throw new UnsupportedOperationException("CmsOrCat has no Java PER decode fallback");
    }

    public static CmsOrCat from(byte[] data) {
        return new CmsOrCat().decode(data);
    }
}
