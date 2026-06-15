package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.enumerated.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * RpcMethodDefChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     method      [1] IMPLICIT RpcMethodDef
 * }  —  8.13.5
 *
 * Used by GetRpcMethodDefinition response.
 */
public class CmsRpcMethodDefChoice extends CmsType {

    public static final int ERROR  = 0;
    public static final int METHOD = 1;

    public CmsEnumerated    choice;       /* 0=error, 1=method */
    public CmsServiceError  altError;
    public CmsRpcMethodDef  altMethod;

    public CmsRpcMethodDefChoice() {
        this.choice   = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altMethod = new CmsRpcMethodDef();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altMethod);
    }
}
