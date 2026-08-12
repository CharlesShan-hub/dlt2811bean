package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.data.InnerGetServerDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetServerDirectory-ErrorPDU ::= ServiceError — 8.3.1
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetServerDirectoryError extends CmsServiceError {

    public CmsGetServerDirectoryError() {
        super(new InnerGetServerDirectoryErrorPDU());
    }

    public CmsGetServerDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetServerDirectoryError value(int v) {
        super.value(v);
        return this;
    }

}
