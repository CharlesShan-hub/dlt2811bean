package com.ysh.jcms.services.connect;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;
import com.ysh.jcms.services.type.CmsFFIServices;

/**
 * AbortReason — constrained INTEGER (0..5).
 */
public class CmsAbortReason extends AbstractCmsEnumerated<CmsAbortReason> {

    public static final int OTHER                         = 0;
    public static final int UNRECOGNIZED_SERVICE          = 1;
    public static final int INVALID_REQ_ID                = 2;
    public static final int INVALID_ARGUMENT              = 3;
    public static final int INVALID_RESULT                = 4;
    public static final int MAX_SERV_OUTSTANDING_EXCEEDED = 5;

    public CmsAbortReason() {
        this(0);
    }

    public CmsAbortReason(int value) {
        super("AbortReason", value, 6);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIServices.INSTANCE.cms_abort_reason_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 5);
    }

    public static CmsAbortReason decode(byte[] data) {
        if (CmsFFIDatatypes.isAvailable()) {
            IntByReference v = new IntByReference();
            CmsFFIServices.INSTANCE.cms_abort_reason_decode(data, data.length, v);
            return new CmsAbortReason(v.getValue());
        }
        return new CmsAbortReason((int) PerInteger.decode(new PerInputStream(data), 0, 5));
    }
}
