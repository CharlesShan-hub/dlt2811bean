package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerRpcCallErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * RpcCall-ErrorPDU ::= ServiceError — 8.13.6
 *
 * <p>Type alias, not a SEQUENCE.
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
