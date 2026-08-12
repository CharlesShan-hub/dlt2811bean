package com.ysh.jcms.core.data.sequence.rpc;

import com.ysh.jcms.data.InnerAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt32U;

/**
 * <pre>
 * {@code
 * RpcMethodDef ::= SEQUENCE {
 *     version     [0] IMPLICIT INT32U,
 *     timeout     [1] IMPLICIT INT32U,
 *     request     [2] IMPLICIT DataDefinition,
 *     response    [3] IMPLICIT DataDefinition
 * } — 8.13.5
 * }
 * </pre>
 *
 * <p>
 * inline within GetRpcMethodDefinition-ResponsePDU {@code method} variant
 * (SEQUENCE OF CHOICE element).
 */
public class CmsRpcMethodDef extends CmsSequence {

    @CmsField
    public CmsInt32U version;
    @CmsField
    public CmsInt32U timeout;
    @CmsField
    public CmsDataDefinition request;
    @CmsField
    public CmsDataDefinition response;

    public CmsRpcMethodDef() {
        super(new InnerAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod());
    }

    public CmsRpcMethodDef version(long v) {
        this.version.value(v);
        return this;
    }
    public CmsRpcMethodDef timeout(long v) {
        this.timeout.value(v);
        return this;
    }
    public CmsRpcMethodDef request(CmsDataDefinition v) {
        this.request.value(v);
        return this;
    }
    public CmsRpcMethodDef response(CmsDataDefinition v) {
        this.response.value(v);
        return this;
    }

    /** Copy all field values from another CmsRpcMethodDef (fluent). */
    public CmsRpcMethodDef value(CmsRpcMethodDef v) {
        return version(v.version.value()).timeout(v.timeout.value()).request(v.request).response(v.response);
    }
}
