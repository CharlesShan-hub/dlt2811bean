package com.ysh.jcms.core.pdu.file;

import com.ysh.jcms.data.InnerGetFileDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetFileDirectory-ErrorPDU ::= ServiceError — 8.12.5
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetFileDirectoryError extends CmsServiceError {

    public CmsGetFileDirectoryError() {
        super(new InnerGetFileDirectoryErrorPDU());
    }

    public CmsGetFileDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetFileDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
