package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetLogicalNodeDirectory-ErrorPDU ::= ServiceError — 8.3.3
 *
 * <p>Type alias, not a SEQUENCE.
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
