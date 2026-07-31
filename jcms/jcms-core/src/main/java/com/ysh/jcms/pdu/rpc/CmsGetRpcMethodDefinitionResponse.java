package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDefinitionResponsePDU;
import com.ysh.jcms.data.choice.CmsRpcMethodDefChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
 *     reference   [0] IMPLICIT SEQUENCE OF RpcMethodDefChoice,
 *     moreFollows [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.13.5
 */
public class CmsGetRpcMethodDefinitionResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsRpcMethodDefChoice.class)
    public List<CmsRpcMethodDefChoice> reference; /* SEQUENCE OF RpcMethodDefChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcMethodDefinitionResponse() {
        super(new InnerGetRpcMethodDefinitionResponsePDU());
        this.reference = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetRpcMethodDefinitionResponse reference(List<CmsRpcMethodDefChoice> v) {
        this.reference = v;
        return this;
    }
    public CmsGetRpcMethodDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
