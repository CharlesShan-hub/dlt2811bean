package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsNumeric;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

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
        return CmsFFIDatatypes.Holder.INSTANCE.cms_boolean_encode(value ? 1 : 0, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encodeBoolean(pos, value);
    }

    public static CmsBoolean decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            IntByReference v = new IntByReference();
            CmsFFIDatatypes.Holder.INSTANCE.cms_boolean_decode(data, data.length, v);
            return v.getValue() != 0 ? TRUE : FALSE;
        }
        return PerInteger.decodeBoolean(new PerInputStream(data)) ? TRUE : FALSE;
    }
}
