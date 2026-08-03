package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDefinitionErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetRpcMethodDefinition-ErrorPDU ::= ServiceError — 8.13.5
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetRpcMethodDefinitionError extends CmsServiceError {

    public CmsGetRpcMethodDefinitionError() {
        super(new InnerGetRpcMethodDefinitionErrorPDU());
    }

    public CmsGetRpcMethodDefinitionError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetRpcMethodDefinitionError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
