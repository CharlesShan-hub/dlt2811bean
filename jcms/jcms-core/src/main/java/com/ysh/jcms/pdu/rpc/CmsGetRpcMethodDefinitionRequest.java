package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDefinitionRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

import java.util.ArrayList;
import java.util.List;

/**
 * GetRpcMethodDefinition-RequestPDU ::= SEQUENCE { reference [0] IMPLICIT
 * SEQUENCE OF VisibleString } — 8.13.5
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
