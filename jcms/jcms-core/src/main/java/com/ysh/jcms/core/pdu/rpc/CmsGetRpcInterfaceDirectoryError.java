package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError — 8.13.2
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetRpcInterfaceDirectoryError extends CmsServiceError {

    public CmsGetRpcInterfaceDirectoryError() {
        super(new InnerGetRpcInterfaceDirectoryErrorPDU());
    }

    public CmsGetRpcInterfaceDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetRpcInterfaceDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
