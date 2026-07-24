package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * RpcMethodDefChoice ::= CHOICE { error [0] IMPLICIT ServiceError, method [1]
 * IMPLICIT RpcMethodDef } — 8.13.5
 *
 * Used by GetRpcMethodDefinition response.
 */
public class CmsRpcMethodDefChoice extends CmsTypeOld {

    public static final int ERROR = 0;
    public static final int METHOD = 1;

    public CmsEnumerated choice; /* 0=error, 1=method */
    public CmsServiceError altError;
    public CmsRpcMethodDef altMethod;

    public CmsRpcMethodDefChoice() {
        this.choice = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altMethod = new CmsRpcMethodDef();
    }

    public CmsRpcMethodDefChoice choice(int v) {
        this.choice.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(choice, altError, altMethod);
    }
}
