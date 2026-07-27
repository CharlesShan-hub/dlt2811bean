package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryErrorPDU;

/**
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryError extends CmsType {

    public int serviceError;

    public CmsGetLogicalDeviceDirectoryError() {
        super(new InnerGetLogicalDeviceDirectoryErrorPDU());
    }

    public CmsGetLogicalDeviceDirectoryError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetLogicalDeviceDirectoryErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetLogicalDeviceDirectoryErrorPDU) inner).value;
    }
}
