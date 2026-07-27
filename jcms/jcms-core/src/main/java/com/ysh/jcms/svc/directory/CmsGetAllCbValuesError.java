package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetAllCBValuesErrorPDU;

/**
 * GetAllCBValues-ErrorPDU ::= ServiceError — 8.3.6
 */
public class CmsGetAllCbValuesError extends CmsType {

    public int serviceError;

    public CmsGetAllCbValuesError() {
        super(new InnerGetAllCBValuesErrorPDU());
    }

    public CmsGetAllCbValuesError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetAllCBValuesErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetAllCBValuesErrorPDU) inner).value;
    }
}
