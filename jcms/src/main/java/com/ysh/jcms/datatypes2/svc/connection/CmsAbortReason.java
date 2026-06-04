package com.ysh.jcms.datatypes2.svc.connection;

/**
 * cms_abort_reason_t — INTEGER (0..5).
 *
 * C: typedef enum { ... } cms_abort_reason_t;
 */
public class CmsAbortReason {
    private CmsAbortReason() {}

    public static final int OTHER                        = 0;
    public static final int UNRECOGNIZED_SERVICE         = 1;
    public static final int INVALID_REQ_ID               = 2;
    public static final int INVALID_ARGUMENT             = 3;
    public static final int INVALID_RESULT               = 4;
    public static final int MAX_SERV_OUTSTANDING_EXCEEDED = 5;
}
