package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.data.InnerGetDataDefinitionErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetDataDefinition-ErrorPDU ::= ServiceError — 8.4.4
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
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
