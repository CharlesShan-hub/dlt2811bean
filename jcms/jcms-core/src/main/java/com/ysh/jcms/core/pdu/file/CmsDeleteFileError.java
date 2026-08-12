package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.data.InnerDeleteFileErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * DeleteFile-ErrorPDU ::= ServiceError — 8.12.3
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsDeleteFileError extends CmsServiceError {

    public CmsDeleteFileError() {
        super(new InnerDeleteFileErrorPDU());
    }

    public CmsDeleteFileError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsDeleteFileError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
