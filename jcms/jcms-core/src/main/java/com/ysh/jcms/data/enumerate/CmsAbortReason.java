package com.ysh.jcms.data.enumerate;

import com.ysh.jcms.data.core.CmsEnum;

/**
 * AbortReason ::= INTEGER { other (0), unrecognized-service (1), invalid-reqID
 * (2), invalid-argument (3), invalid-result (4), max-serv-outstanding-exceeded
 * (5) } (0..5) — 8.2.3 PER: constrained integer (0..5), 3 bits
 */
@CmsEnum.ValueRange(min = 0, max = 5)
public class CmsAbortReason extends CmsEnum<CmsAbortReason> {

    public static final int OTHER = 0;
    public static final int UNRECOGNIZED_SERVICE = 1;
    public static final int INVALID_REQ_ID = 2;
    public static final int INVALID_ARGUMENT = 3;
    public static final int INVALID_RESULT = 4;
    public static final int MAX_SERV_OUTSTANDING_EXCEEDED = 5;

    public CmsAbortReason() {}
    public CmsAbortReason(int value) { value(value); }
}
