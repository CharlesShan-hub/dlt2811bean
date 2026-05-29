package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsServiceError extends AbstractCmsEnumerated {

    public CmsServiceError() {
        this(0);
    }

    public CmsServiceError(int value) {
        super("ServiceError", value, 13);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_ServiceError(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsServiceError decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_ServiceError(data, data.length, v);
        return new CmsServiceError(v.getValue());
    }

    @Override
    public CmsServiceError copy() {
        CmsServiceError clone = new CmsServiceError();
        return copyTo(clone);
    }
}
