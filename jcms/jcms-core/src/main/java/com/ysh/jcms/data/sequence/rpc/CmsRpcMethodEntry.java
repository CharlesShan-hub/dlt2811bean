package com.ysh.jcms.data.sequence.rpc;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousGetRpcInterfaceDefinitionResponsePDUMethod;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * RpcMethodEntry (inline within GetRpcInterfaceDefinition-ResponsePDU
 * {@code method} SEQUENCE OF) ::= SEQUENCE { name [0] IMPLICIT VisibleString,
 * version [1] IMPLICIT Int32U, timeout [2] IMPLICIT Int32U, request [3]
 * IMPLICIT DataDefinition, response [4] IMPLICIT DataDefinition } — 8.13.4
 */
public class CmsRpcMethodEntry extends CmsSequence {

    @CmsField
    public CmsString name;
    @CmsField
    public CmsInt32U version;
    @CmsField
    public CmsInt32U timeout;
    @CmsField
    public CmsDataDefinition request;
    @CmsField
    public CmsDataDefinition response;

    public CmsRpcMethodEntry() {
        super(new InnerAnonymousGetRpcInterfaceDefinitionResponsePDUMethod());
    }

    public CmsRpcMethodEntry name(String v) {
        this.name.value(v);
        return this;
    }
    public CmsRpcMethodEntry name(byte[] v) {
        return name(new String(v, StandardCharsets.UTF_8));
    }
    public CmsRpcMethodEntry version(long v) {
        this.version.value(v);
        return this;
    }
    public CmsRpcMethodEntry timeout(long v) {
        this.timeout.value(v);
        return this;
    }
    public CmsRpcMethodEntry request(CmsDataDefinition v) {
        this.request.value(v);
        return this;
    }
    public CmsRpcMethodEntry response(CmsDataDefinition v) {
        this.response.value(v);
        return this;
    }

    /** Copy all field values from another CmsRpcMethodEntry (fluent). */
    public CmsRpcMethodEntry value(CmsRpcMethodEntry v) {
        return name(v.name.value()).version(v.version.value()).timeout(v.timeout.value()).request(v.request).response(v.response);
    }
}
