package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.InnerGetAllDataDefinitionErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetAllDataDefinition-ErrorPDU ::= ServiceError — 8.3.5
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetAllDataDefinitionError extends CmsServiceError {

    public CmsGetAllDataDefinitionError() {
        super(new InnerGetAllDataDefinitionErrorPDU());
    }

    public CmsGetAllDataDefinitionError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetAllDataDefinitionError value(int v) {
        super.value(v);
        return this;
    }

}
