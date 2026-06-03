package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import java.nio.charset.StandardCharsets;

public class CmsVisibleString extends AbstractCmsString<CmsVisibleString, String> {

    public CmsVisibleString() {
        super("VisibleString", "");
    }

    public CmsVisibleString(String value) {
        this();
        this.value = value != null ? value : "";
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        if (isFixed()) {
            return CmsFFIDatatypes.INSTANCE.cms_visible_string_encode(value, size, 0, buf, outLen);
        } else if (isVariable()) {
            return CmsFFIDatatypes.INSTANCE.cms_visible_string_encode(value, 0, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    public static CmsVisibleString decode(byte[] data, Mode mode, int length) {
        byte[] strBuf = new byte[MAX_ENCODE_BUF_SIZE];
        IntByReference strLen = new IntByReference(strBuf.length);
        int sizeLen = mode == Mode.FIXED ? length : 0;
        int maxLen = mode == Mode.VARIABLE ? length : 0;
        CmsFFIDatatypes.INSTANCE.cms_visible_string_decode(data, data.length, sizeLen, maxLen, strBuf, strLen);
        int len = strLen.getValue();
        if (len >= strBuf.length) len = 0;
        CmsVisibleString vs = new CmsVisibleString(new String(strBuf, 0, len, StandardCharsets.US_ASCII));
        if (mode == Mode.FIXED) vs.size(length);
        else vs.max(length);
        return vs;
    }
}
