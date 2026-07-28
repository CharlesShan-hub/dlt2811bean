package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError — 8.3.2
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetLogicalDeviceDirectoryError extends CmsServiceError {

    public CmsGetLogicalDeviceDirectoryError() {
        super(new InnerGetLogicalDeviceDirectoryErrorPDU());
    }

    @Override
    public CmsGetLogicalDeviceDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
