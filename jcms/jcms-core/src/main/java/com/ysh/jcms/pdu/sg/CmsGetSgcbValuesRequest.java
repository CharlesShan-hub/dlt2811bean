package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerGetSGCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * GetSGCBValues-RequestPDU ::= SEQUENCE { sgcbReference [0] IMPLICIT SEQUENCE
 * OF ObjectReference } — 8.6.6
 */
public class CmsGetSgcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> sgcbReference; /* SEQUENCE OF ObjectReference */

    public CmsGetSgcbValuesRequest() {
        super(new InnerGetSGCBValuesRequestPDU());
        this.sgcbReference = new ArrayList<>();
    }

    public CmsGetSgcbValuesRequest sgcbReference(List<CmsObjectReference> v) {
        this.sgcbReference = v;
        return this;
    }
}
