package com.ysh.jcms.pdu.file;

import com.ysh.jcms.data.InnerGetFileErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetFile-ErrorPDU ::= ServiceError — 8.12.1
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetFileError extends CmsServiceError {

    public CmsGetFileError() {
        super(new InnerGetFileErrorPDU());
    }

    public CmsGetFileError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetFileError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
