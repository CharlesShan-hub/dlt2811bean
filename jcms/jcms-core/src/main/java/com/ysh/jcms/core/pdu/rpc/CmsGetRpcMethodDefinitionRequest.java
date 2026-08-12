package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDefinitionRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsString;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString
 * } — 8.13.5
 * }
 * </pre>
 */
public class CmsGetRpcMethodDefinitionRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsString.class)
    public List<CmsString> reference; /* SEQUENCE OF VisibleString */

    public CmsGetRpcMethodDefinitionRequest() {
        super(new InnerGetRpcMethodDefinitionRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetRpcMethodDefinitionRequest reference(List<CmsString> v) {
        this.reference = v;
        return this;
    }
}
