package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsBoolean extends AbstractCmsNumeric<CmsBoolean, Boolean> {

    public static final CmsBoolean TRUE = new CmsBoolean(true);
    public static final CmsBoolean FALSE = new CmsBoolean(false);

    public CmsBoolean() {
        this(false);
    }

    public CmsBoolean(boolean value) {
        super("BOOLEAN", value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_boolean_encode(value ? 1 : 0, buf, outLen);
    }

    public static CmsBoolean decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_boolean_decode(data, data.length, v);
        return v.getValue() != 0 ? TRUE : FALSE;
    }
}
