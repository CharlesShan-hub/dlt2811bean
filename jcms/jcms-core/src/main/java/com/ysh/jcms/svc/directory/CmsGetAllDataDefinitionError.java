package com.ysh.jcms.svc.directory;

import com.ysh.jcms.data.InnerGetAllDataDefinitionErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * GetAllDataDefinition-ErrorPDU ::= ServiceError — 8.3.5
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetAllDataDefinitionError extends CmsServiceError {

    public CmsGetAllDataDefinitionError() {
        super(new InnerGetAllDataDefinitionErrorPDU());
    }

    @Override
    public CmsGetAllDataDefinitionError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
