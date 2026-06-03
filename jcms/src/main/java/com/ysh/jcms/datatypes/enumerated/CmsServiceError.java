package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsServiceError extends AbstractCmsEnumerated<CmsServiceError> {

    public static final int NO_ERROR                                      = 0;
    public static final int INSTANCE_NOT_AVAILABLE                        = 1;
    public static final int INSTANCE_IN_USE                               = 2;
    public static final int ACCESS_VIOLATION                              = 3;
    public static final int ACCESS_NOT_ALLOWED_IN_CURRENT_STATE           = 4;
    public static final int PARAMETER_VALUE_INAPPROPRIATE                 = 5;
    public static final int PARAMETER_VALUE_INCONSISTENT                  = 6;
    public static final int CLASS_NOT_SUPPORTED                           = 7;
    public static final int INSTANCE_LOCKED_BY_OTHER_CLIENT               = 8;
    public static final int CONTROL_MUST_BE_SELECTED                      = 9;
    public static final int TYPE_CONFLICT                                 = 10;
    public static final int FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT       = 11;
    public static final int FAILED_DUE_TO_SERVER_CONSTRAINT               = 12;

    public CmsServiceError() {
        this(0);
    }

    public CmsServiceError(int value) {
        super("ServiceError", value, 13);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_service_error_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 12);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.Holder.INSTANCE.cms_service_error_decode(data, data.length, v);
        this.value = v.getValue();
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = (int) PerInteger.decode(pis, 0, size - 1);
    }

    public static CmsServiceError from(byte[] data) {
        return new CmsServiceError().decode(data);
    }
}
