package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * RpcMethodDef ::= SEQUENCE {
 *     version     [0] IMPLICIT INT32U,
 *     timeout     [1] IMPLICIT INT32U,
 *     request     [2] IMPLICIT DataDefinition,
 *     response    [3] IMPLICIT DataDefinition
 * }  —  8.13.5
 *
 * Used by GetRpcMethodDefinition response (inside CHOICE).
 */
public class CmsRpcMethodDef extends CmsType {

    public CmsInt32U        version;
    public CmsInt32U        timeout;
    public CmsDataDefinition request;
    public CmsDataDefinition response;

    public CmsRpcMethodDef() {
        this.version  = new CmsInt32U();
        this.timeout  = new CmsInt32U();
        this.request  = new CmsDataDefinition();
        this.response = new CmsDataDefinition();
    }
    
    // -- chain setters --
    public CmsRpcMethodDef version(long v) { this.version.value(v); return this; }
    public CmsRpcMethodDef timeout(long v) { this.timeout.value(v); return this; }
    public CmsRpcMethodDef request(CmsDataDefinition v) { this.request = v; return this; }
    public CmsRpcMethodDef response(CmsDataDefinition v) { this.response = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(version, timeout, request, response);
    }
}