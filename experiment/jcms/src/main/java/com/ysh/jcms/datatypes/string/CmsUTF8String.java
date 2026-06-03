package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerUtf8String;
import java.nio.charset.StandardCharsets;

public class CmsUTF8String extends AbstractCmsString<CmsUTF8String, String> {

    public CmsUTF8String() {
        super("UTF8String", "");
    }

    public CmsUTF8String(String value) {
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
        byte[] utf8Bytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] nullTerminated = new byte[utf8Bytes.length + 1];
        System.arraycopy(utf8Bytes, 0, nullTerminated, 0, utf8Bytes.length);
        nullTerminated[utf8Bytes.length] = 0;
        if (isFixed()) {
            return CmsFFIDatatypes.Holder.INSTANCE.cms_utf8_string_encode(nullTerminated, size, 0, buf, outLen);
        } else if (isVariable()) {
            return CmsFFIDatatypes.Holder.INSTANCE.cms_utf8_string_encode(nullTerminated, 0, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        if (isFixed()) {
            PerUtf8String.encodeFixedSize(pos, value, size);
        } else if (isVariable()) {
            PerUtf8String.encodeConstrained(pos, value, 0, max);
        }
    }

    public static CmsUTF8String decode(byte[] data, Mode mode, int length) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] strBuf = new byte[MAX_ENCODE_BUF_SIZE];
           IntByReference strLen = new IntByReference(strBuf.length);
           int sizeLen = mode == Mode.FIXED ? length : 0;
           int maxLen = mode == Mode.VARIABLE ? length : 0;
           CmsFFIDatatypes.Holder.INSTANCE.cms_utf8_string_decode(data, data.length, sizeLen, maxLen, strBuf, strLen);
           int len = strLen.getValue();
           if (len >= strBuf.length) len = 0;
           CmsUTF8String us = new CmsUTF8String(new String(strBuf, 0, len, StandardCharsets.UTF_8));
           if (mode == Mode.FIXED) us.size(length);
           else us.max(length);
           return us;
       }
        if (mode == Mode.FIXED) {
            return new CmsUTF8String(PerUtf8String.decodeFixedSize(new PerInputStream(data), length));
        }
        return new CmsUTF8String(PerUtf8String.decodeConstrained(new PerInputStream(data), 0, length));
    }
}
