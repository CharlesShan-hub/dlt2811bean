package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError — 8.13.2
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
