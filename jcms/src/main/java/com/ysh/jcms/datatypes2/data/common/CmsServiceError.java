package com.ysh.jcms.datatypes2.data.common;

/**
 * cms_service_error_t — INTEGER (0..20), service error code.
 *
 * C: typedef int cms_service_error_t;
 */
public class CmsServiceError {
    private CmsServiceError() {}

    public static final int OTHER                       = 0;
    public static final int TYPE_UNSUPPORTED            = 1;
    public static final int INSTANCE_NOT_AVAILABLE      = 2;
    public static final int INSTANCE_IN_USE             = 3;
    public static final int ACCESS_DENIED               = 4;
    public static final int INSTANCE_LOCKED_BY_OTHER    = 5;
    public static final int VALUE_NOT_VALID             = 6;
    public static final int OBJECT_UNDEFINED            = 7;
    public static final int HARDWARE_FAULT              = 8;
    public static final int TEMPORARY_FAILURE           = 9;
    public static final int OBJECT_EXISTS               = 10;
    public static final int OBJECT_NOT_EXISTS           = 11;
    public static final int OBJECT_IS_NULL              = 12;
    public static final int OUT_OF_MEMORY               = 13;
    public static final int PARAMETER_VALUE_INCONSISTENT = 14;
    public static final int PARAMETER_VALUE_INVALID     = 15;
    public static final int INSTANCE_NOT_EXISTS         = 16;
    public static final int DATA_SET_NOT_EXISTS         = 17;
    public static final int DATA_SET_SIGNED             = 18;
    public static final int DATA_SET_NOT_SIGNED         = 19;
}
