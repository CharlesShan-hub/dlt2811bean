package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.InnerGetServerDirectoryErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * GetServerDirectory-ErrorPDU ::= ServiceError — 8.3.1
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetServerDirectoryError extends CmsServiceError {

    public CmsGetServerDirectoryError() {
        super(new InnerGetServerDirectoryErrorPDU());
    }

    @Override
    public CmsGetServerDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
