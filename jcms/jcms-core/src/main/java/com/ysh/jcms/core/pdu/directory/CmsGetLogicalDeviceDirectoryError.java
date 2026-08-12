package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError — 8.3.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
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
