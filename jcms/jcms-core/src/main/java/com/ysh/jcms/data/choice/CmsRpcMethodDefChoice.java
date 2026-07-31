package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetRpcMethodDefinitionResponsePDUReference;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.rpc.CmsRpcMethodDef;

/**
 * RpcMethodDefChoice (inline within GetRpcMethodDefinition-ResponsePDU
 * {@code reference} SEQUENCE OF) ::= CHOICE {
 *     error   [0] IMPLICIT ServiceError,
 *     method  [1] IMPLICIT RpcMethodDef
 * } — 8.13.5
 */
public class CmsRpcMethodDefChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int METHOD = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER) public CmsServiceError altError;
    @Choice(index = 1, name = "method", sync = Sync.WRAPPER) public CmsRpcMethodDef altMethod;

    public CmsRpcMethodDefChoice() {
        super(new InnerAnonymousGetRpcMethodDefinitionResponsePDUReference());
    }

    public CmsRpcMethodDefChoice choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsRpcMethodDefChoice altError(int v) { choice(ERROR); this.altError.value(v); return this; }
    public CmsRpcMethodDefChoice altMethod(CmsRpcMethodDef v) { choice(METHOD); this.altMethod.value(v); return this; }

    /** Copy choice selection and value from another CmsRpcMethodDefChoice (fluent). */
    public CmsRpcMethodDefChoice value(CmsRpcMethodDefChoice v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case ERROR: this.altError.value(v.altError.value()); break;
            case METHOD: this.altMethod.value(v.altMethod); break;
        }
        return this;
    }
}
