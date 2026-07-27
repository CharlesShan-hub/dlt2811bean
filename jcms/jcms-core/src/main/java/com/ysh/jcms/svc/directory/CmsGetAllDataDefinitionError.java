package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetAllDataDefinitionErrorPDU;

/**
 * GetAllDataDefinition-ErrorPDU ::= ServiceError — 8.3.5
 */
public class CmsGetAllDataDefinitionError extends CmsType {

    public int serviceError;

    public CmsGetAllDataDefinitionError() {
        super(new InnerGetAllDataDefinitionErrorPDU());
    }

    public CmsGetAllDataDefinitionError serviceError(int v) {
        this.serviceError = v;
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerGetAllDataDefinitionErrorPDU) inner).value = serviceError;
    }

    @Override
    public void syncFromInner() {
        this.serviceError = ((InnerGetAllDataDefinitionErrorPDU) inner).value;
    }
}
