package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerVisibleString;
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
    protected int encodeBufSize() {
        return MAX_ENCODE_BUF_SIZE;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        if (isFixed()) {
            return CmsFFIDatatypes.Holder.INSTANCE.cms_visible_string_encode(value, size, 0, buf, outLen);
        } else if (isVariable()) {
            return CmsFFIDatatypes.Holder.INSTANCE.cms_visible_string_encode(value, 0, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        if (isFixed()) {
            PerVisibleString.encodeFixedSize(pos, value, size);
        } else if (isVariable()) {
            PerVisibleString.encodeConstrained(pos, value, 0, max);
        }
    }

    public static CmsVisibleString decode(byte[] data, Mode mode, int length) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] strBuf = new byte[MAX_ENCODE_BUF_SIZE];
           IntByReference strLen = new IntByReference(strBuf.length);
           int sizeLen = mode == Mode.FIXED ? length : 0;
           int maxLen = mode == Mode.VARIABLE ? length : 0;
           CmsFFIDatatypes.Holder.INSTANCE.cms_visible_string_decode(data, data.length, sizeLen, maxLen, strBuf, strLen);
           int len = strLen.getValue();
           if (len >= strBuf.length) len = 0;
           CmsVisibleString vs = new CmsVisibleString(new String(strBuf, 0, len, StandardCharsets.US_ASCII));
           if (mode == Mode.FIXED) vs.size(length);
           else vs.max(length);
           return vs;
       }
        int intLen = mode == Mode.FIXED ? length : 0;
        int maxLen = mode == Mode.VARIABLE ? length : 0;
        if (mode == Mode.FIXED) {
            return new CmsVisibleString(PerVisibleString.decodeFixedSize(new PerInputStream(data), intLen));
        }
        return new CmsVisibleString(PerVisibleString.decodeConstrained(new PerInputStream(data), 0, maxLen));
    }
}
