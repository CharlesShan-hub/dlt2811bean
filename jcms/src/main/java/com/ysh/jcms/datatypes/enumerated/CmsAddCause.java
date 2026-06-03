package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsEnumerated;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerInteger;

public class CmsAddCause extends AbstractCmsEnumerated<CmsAddCause> {

    public static final int UNKNOWN                        = 0;
    public static final int NOT_SUPPORTED                  = 1;
    public static final int BLOCKED_BY_SWITCHING_HIERARCHY = 2;
    public static final int SELECT_FAILED                  = 3;
    public static final int INVALID_POSITION               = 4;
    public static final int POSITION_REACHED               = 5;
    public static final int PARAMETER_CHANGE_IN_EXECUTION  = 6;
    public static final int STEP_LIMIT                     = 7;
    public static final int BLOCKED_BY_MODE                = 8;
    public static final int BLOCKED_BY_PROCESS             = 9;
    public static final int BLOCKED_BY_INTERLOCKING        = 10;
    public static final int BLOCKED_BY_SYNCHECK            = 11;
    public static final int COMMAND_ALREADY_IN_EXECUTION   = 12;
    public static final int BLOCKED_BY_HEALTH              = 13;
    public static final int ONE_OF_A_CONTROL               = 14;
    public static final int ABORTION_BY_CANCEL             = 15;
    public static final int TIME_LIMIT_OVER                = 16;
    public static final int ABORTION_BY_TRIP               = 17;
    public static final int OBJECT_NOT_SELECTED            = 18;
    public static final int OBJECT_ALREADY_SELECTED        = 19;
    public static final int NO_ACCESS_AUTHORITY            = 20;
    public static final int ENDED_WITH_OVERSHOOT           = 21;
    public static final int ABORTION_DUE_TO_DEVIATION      = 22;
    public static final int ABORTION_BY_COMMUNICATION_LOSS = 23;
    public static final int BLOCKED_BY_COMMAND             = 24;
    public static final int NONE                           = 25;
    public static final int LOCKED_BY_OTHER_CLIENT         = 26;
    public static final int INCONSISTENT_PARAMETERS        = 27;

    public CmsAddCause() {
        this(UNKNOWN);
    }

    public CmsAddCause(int value) {
        super("AddCause", value, 28);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_add_cause_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerInteger.encode(pos, value, 0, 27);
    }

    public static CmsAddCause decode(byte[] data) {
       if (CmsFFIDatatypes.isAvailable()) {
           IntByReference v = new IntByReference();
           CmsFFIDatatypes.Holder.INSTANCE.cms_add_cause_decode(data, data.length, v);
           return new CmsAddCause(v.getValue());
       }
        return new CmsAddCause((int) PerInteger.decode(new PerInputStream(data), 0, 27));
    }
}
