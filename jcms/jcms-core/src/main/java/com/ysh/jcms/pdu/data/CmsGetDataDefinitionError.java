package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerGetDataDefinitionErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetDataDefinition-ErrorPDU ::= ServiceError — 8.4.4
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsGetDataDefinitionError extends CmsServiceError {

    public CmsGetDataDefinitionError() {
        super(new InnerGetDataDefinitionErrorPDU());
    }

    public CmsGetDataDefinitionError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetDataDefinitionError value(int v) {
        super.value(v);
        return this;
    }
}
