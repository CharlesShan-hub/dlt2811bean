package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetLogicalNodeDirectory-ErrorPDU ::= ServiceError — 8.3.3
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetLogicalNodeDirectoryError extends CmsServiceError {

    public CmsGetLogicalNodeDirectoryError() {
        super(new InnerGetLogicalNodeDirectoryErrorPDU());
    }

    public CmsGetLogicalNodeDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetLogicalNodeDirectoryError value(int v) {
        super.value(v);
        return this;
    }

}
