package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDefinitionErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetRpcInterfaceDefinition-ErrorPDU ::= ServiceError — 8.13.4
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetRpcInterfaceDefinitionError extends CmsServiceError {

    public CmsGetRpcInterfaceDefinitionError() {
        super(new InnerGetRpcInterfaceDefinitionErrorPDU());
    }

    public CmsGetRpcInterfaceDefinitionError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetRpcInterfaceDefinitionError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
