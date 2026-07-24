package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerServiceError;

/**
 * ServiceError ::= INTEGER (0..12) — 7.3.11 PER: constrained integer (0..12), 4 bits.
 */
public class CmsServiceError extends CmsType {

    public static final int NO_ERROR = 0;
    public static final int INSTANCE_NOT_AVAILABLE = 1;
    public static final int INSTANCE_IN_USE = 2;
    public static final int ACCESS_VIOLATION = 3;
    public static final int ACCESS_NOT_ALLOWED_IN_CURRENT_STATE = 4;
    public static final int PARAMETER_VALUE_INAPPROPRIATE = 5;
    public static final int PARAMETER_VALUE_INCONSISTENT = 6;
    public static final int CLASS_NOT_SUPPORTED = 7;
    public static final int INSTANCE_LOCKED_BY_OTHER_CLIENT = 8;
    public static final int CONTROL_MUST_BE_SELECTED = 9;
    public static final int TYPE_CONFLICT = 10;
    public static final int FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT = 11;
    public static final int FAILED_DUE_TO_SERVER_CONSTRAINT = 12;

    public CmsServiceError() {
        super(new InnerServiceError());
    }
    public CmsServiceError(int value) {
        this();
        value(value);
    }

    public int value() {
        return ((InnerServiceError) inner).value;
    }
    public CmsServiceError value(int v) {
        if (v < 0 || v > 12)
            throw new IllegalArgumentException("CmsServiceError out of range [0,12]: " + v);
        ((InnerServiceError) inner).value = v;
        return this;
    }
}
