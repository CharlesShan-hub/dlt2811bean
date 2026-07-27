package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryErrorPDU;

/**
 * GetLogicalNodeDirectory-ErrorPDU ::= ServiceError — 8.3.3
 */
public class CmsGetLogicalNodeDirectoryError extends CmsType {

    public int serviceError;

    public CmsGetLogicalNodeDirectoryError() {
        super(new InnerGetLogicalNodeDirectoryErrorPDU());
    }

    public CmsGetLogicalNodeDirectoryError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetLogicalNodeDirectoryErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetLogicalNodeDirectoryErrorPDU) inner).value;
    }
}
