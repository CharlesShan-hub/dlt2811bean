package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsAddCause extends AbstractCmsEnumerated {

    public static final int UNKNOWN                    = 0;
    public static final int PROCESS_ERROR              = 1;
    public static final int PROTOCOL_ERROR             = 2;
    public static final int APPLICATION_ERROR          = 3;
    public static final int PERFORMANCE_LIMITATION     = 4;
    public static final int RESOURCE_LIMITATION        = 5;
    public static final int AUTHENTICATION_FAILURE     = 6;
    public static final int SECURITY_VIOLATION         = 7;
    public static final int COMMUNICATION_FAILURE      = 8;
    public static final int SYSTEM_FAILURE             = 9;
    public static final int HARDWARE_FAILURE           = 10;
    public static final int SOFTWARE_FAILURE           = 11;
    public static final int CONFIGURATION_ERROR        = 12;
    public static final int OPERATION_NOT_SUPPORTED    = 13;
    public static final int OPERATION_BLOCKED          = 14;
    public static final int TEMPORARY_FAILURE          = 15;
    public static final int PERMANENT_FAILURE          = 16;

    public CmsAddCause() {
        this(UNKNOWN);
    }

    public CmsAddCause(int value) {
        super("AddCause", value, 17);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_add_cause_encode(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAddCause decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_add_cause_decode(data, data.length, v);
        return new CmsAddCause(v.getValue());
    }

    @Override
    public CmsAddCause copy() {
        CmsAddCause clone = new CmsAddCause();
        return copyTo(clone);
    }
}
