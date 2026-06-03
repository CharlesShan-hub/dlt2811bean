package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsOctetString extends AbstractCmsString<CmsOctetString, byte[]> {

    public CmsOctetString() {
        super("OCTET STRING", new byte[0]);
    }

    public CmsOctetString(byte[] value) {
        this();
        this.value = value != null ? value : new byte[0];
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        if (isFixed()) {
            return CmsFFIDatatypes.INSTANCE.cms_octet_string_encode(value, value.length, size, 0, buf, outLen);
        } else if (isVariable()) {
            return CmsFFIDatatypes.INSTANCE.cms_octet_string_encode(value, value.length, 0, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    public static CmsOctetString decode(byte[] data, Mode mode, int length) {
        byte[] valBuf = new byte[MAX_ENCODE_BUF_SIZE];
        IntByReference valLen = new IntByReference(valBuf.length);
        int sizeLen = mode == Mode.FIXED ? length : 0;
        int maxLen = mode == Mode.VARIABLE ? length : 0;
        CmsFFIDatatypes.INSTANCE.cms_octet_string_decode(data, data.length, sizeLen, maxLen, valBuf, valLen);
        int len = valLen.getValue();
        if (len >= valBuf.length) len = 0;
        byte[] result = new byte[len];
        System.arraycopy(valBuf, 0, result, 0, len);
        CmsOctetString os = new CmsOctetString(result);
        if (mode == Mode.FIXED) os.size(length);
        else os.max(length);
        return os;
    }
}
