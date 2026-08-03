package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDefinitionResponsePDU;
import com.ysh.jcms.data.choice.CmsRpcMethodDefChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         method      [1] IMPLICIT SEQUENCE {
 *             version     [0] IMPLICIT INT32U,
 *             timeout     [1] IMPLICIT INT32U,
 *             request     [2] IMPLICIT DataDefinition,
 *             response    [3] IMPLICIT DataDefinition
 *         }
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.13.5
 * }
 * </pre>
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
