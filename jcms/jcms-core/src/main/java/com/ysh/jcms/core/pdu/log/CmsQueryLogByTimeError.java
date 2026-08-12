package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerQueryLogByTimeErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * QueryLogByTime-ErrorPDU ::= ServiceError — 8.8.4
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsQueryLogByTimeError extends CmsServiceError {

    public CmsQueryLogByTimeError() {
        super(new InnerQueryLogByTimeErrorPDU());
    }

    public CmsQueryLogByTimeError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsQueryLogByTimeError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
