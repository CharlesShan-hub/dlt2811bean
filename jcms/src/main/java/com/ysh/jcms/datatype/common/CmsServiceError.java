package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsServiceError extends CmsScalar {
    public static final int NO_ERROR                                   = 0;
    public static final int INSTANCE_NOT_AVAILABLE                     = 1;
    public static final int INSTANCE_IN_USE                            = 2;
    public static final int ACCESS_VIOLATION                           = 3;
    public static final int ACCESS_NOT_ALLOWED_IN_CURRENT_STATE        = 4;
    public static final int PARAMETER_VALUE_INAPPROPRIATE              = 5;
    public static final int PARAMETER_VALUE_INCONSISTENT               = 6;
    public static final int CLASS_NOT_SUPPORTED                        = 7;
    public static final int INSTANCE_LOCKED_BY_OTHER_CLIENT            = 8;
    public static final int CONTROL_MUST_BE_SELECTED                   = 9;
    public static final int TYPE_CONFLICT                              = 10;
    public static final int FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT    = 11;
    public static final int FAILED_DUE_TO_SERVER_CONSTRAINT            = 12;

    public int value;

    public static class ByValue extends CmsServiceError implements Structure.ByValue {}
}