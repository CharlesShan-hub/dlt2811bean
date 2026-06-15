package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsEnumerated;

/**
 * AddCause ::= INTEGER (0..27)  —  7.5.4
 * PER: constrained integer (0..27), 5 bits
 * sizeof = 4
 *
 * Alias for CmsEnumerated with named constants.
 */
public class CmsAddCause extends CmsEnumerated {

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

    public CmsAddCause() {}
    public CmsAddCause(int value) { super(0, 27, value); }
}
