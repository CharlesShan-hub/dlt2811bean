package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDefinitionResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.rpc.CmsRpcMethodEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {
 *     method          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         name        [0] IMPLICIT VisibleString,
 *         version     [1] IMPLICIT INT32U,
 *         timeout     [2] IMPLICIT INT32U,
 *         request     [3] IMPLICIT DataDefinition,
 *         response    [4] IMPLICIT DataDefinition
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.13.4
 * }
 * </pre>
 */
public class CmsGetRpcInterfaceDefinitionResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsRpcMethodEntry.class)
    public List<CmsRpcMethodEntry> method; /* SEQUENCE OF RpcMethodEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcInterfaceDefinitionResponse() {
        super(new InnerGetRpcInterfaceDefinitionResponsePDU());
        this.method = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetRpcInterfaceDefinitionResponse method(List<CmsRpcMethodEntry> v) {
        this.method = v;
        return this;
    }
    public CmsGetRpcInterfaceDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
