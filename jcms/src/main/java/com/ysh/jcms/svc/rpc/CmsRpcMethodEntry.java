package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * RpcMethodEntry ::= SEQUENCE {
 *     name        [0] IMPLICIT VisibleString,
 *     version     [1] IMPLICIT INT32U,
 *     timeout     [2] IMPLICIT INT32U,
 *     request     [3] IMPLICIT DataDefinition,
 *     response    [4] IMPLICIT DataDefinition
 * }  —  8.13.4
 *
 * Used by GetRpcInterfaceDefinition response.
 */
public class CmsRpcMethodEntry extends CmsType {

    public CmsUint8Array    name;
    public CmsInt32U        version;
    public CmsInt32U        timeout;
    public CmsDataDefinition request;
    public CmsDataDefinition response;

    public CmsRpcMethodEntry() {
        this.name     = new CmsUint8Array();
        this.version  = new CmsInt32U();
        this.timeout  = new CmsInt32U();
        this.request  = new CmsDataDefinition();
        this.response = new CmsDataDefinition();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(name, version, timeout, request, response);
    }
}
