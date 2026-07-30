package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerGetDataDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetDataDirectory-ErrorPDU ::= ServiceError — 8.4.3
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetDataDirectoryError extends CmsServiceError {

    public CmsGetDataDirectoryError() {
        super(new InnerGetDataDirectoryErrorPDU());
    }

    public CmsGetDataDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetDataDirectoryError value(int v) {
        super.value(v);
        return this;
    }
}
