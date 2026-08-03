package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDirectoryErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * GetRpcMethodDirectory-ErrorPDU ::= ServiceError — 8.13.3
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsGetRpcMethodDirectoryError extends CmsServiceError {

    public CmsGetRpcMethodDirectoryError() {
        super(new InnerGetRpcMethodDirectoryErrorPDU());
    }

    public CmsGetRpcMethodDirectoryError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsGetRpcMethodDirectoryError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
