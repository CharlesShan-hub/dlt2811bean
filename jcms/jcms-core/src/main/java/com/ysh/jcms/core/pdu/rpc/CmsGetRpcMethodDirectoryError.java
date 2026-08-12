package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDirectoryErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * GetRpcMethodDirectory-ErrorPDU ::= ServiceError — 8.13.3
 * }
 * </pre>
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
