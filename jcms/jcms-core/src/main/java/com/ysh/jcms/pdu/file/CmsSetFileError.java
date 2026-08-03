package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerSetFileErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * SetFile-ErrorPDU ::= ServiceError — 8.12.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsSetFileError extends CmsServiceError {

    public CmsSetFileError() {
        super(new InnerSetFileErrorPDU());
    }

    public CmsSetFileError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsSetFileError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
