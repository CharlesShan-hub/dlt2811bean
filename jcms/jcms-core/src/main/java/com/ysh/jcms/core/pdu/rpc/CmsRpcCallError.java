package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerRpcCallErrorPDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * RpcCall-ErrorPDU ::= ServiceError — 8.13.6
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsRpcCallError extends CmsServiceError {

    public CmsRpcCallError() {
        super(new InnerRpcCallErrorPDU());
    }

    public CmsRpcCallError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsRpcCallError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
