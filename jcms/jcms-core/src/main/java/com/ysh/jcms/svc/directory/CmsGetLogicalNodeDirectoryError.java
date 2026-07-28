package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * GetLogicalNodeDirectory-ErrorPDU ::= ServiceError — 8.3.3
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetLogicalNodeDirectoryError extends CmsServiceError {

    public CmsGetLogicalNodeDirectoryError() {
        super(new InnerGetLogicalNodeDirectoryErrorPDU());
    }

    @Override
    public CmsGetLogicalNodeDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
