package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetAllDataValuesErrorPDU;

/**
 * GetAllDataValues-ErrorPDU ::= ServiceError — 8.3.4
 */
public class CmsGetAllDataValuesError extends CmsType {

    public int serviceError;

    public CmsGetAllDataValuesError() {
        super(new InnerGetAllDataValuesErrorPDU());
    }

    public CmsGetAllDataValuesError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetAllDataValuesErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetAllDataValuesErrorPDU) inner).value;
    }
}
