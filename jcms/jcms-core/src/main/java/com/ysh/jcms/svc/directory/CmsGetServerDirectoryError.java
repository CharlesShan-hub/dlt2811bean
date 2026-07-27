package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetServerDirectoryErrorPDU;

/**
 * GetServerDirectory-ErrorPDU ::= ServiceError — 8.3.1
 */
public class CmsGetServerDirectoryError extends CmsType {

    public int serviceError;

    public CmsGetServerDirectoryError() {
        super(new InnerGetServerDirectoryErrorPDU());
    }

    public CmsGetServerDirectoryError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetServerDirectoryErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetServerDirectoryErrorPDU) inner).value;
    }
}
