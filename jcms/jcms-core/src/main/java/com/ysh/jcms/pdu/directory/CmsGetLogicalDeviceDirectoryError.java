package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError — 8.3.2
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetLogicalDeviceDirectoryError extends CmsServiceError {

    public CmsGetLogicalDeviceDirectoryError() {
        super(new InnerGetLogicalDeviceDirectoryErrorPDU());
    }

    public CmsGetLogicalDeviceDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetLogicalDeviceDirectoryError value(int v) {
        super.value(v);
        return this;
    }

}
