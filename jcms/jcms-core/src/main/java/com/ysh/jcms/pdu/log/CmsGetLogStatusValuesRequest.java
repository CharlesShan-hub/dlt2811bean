package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLogStatusValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * GetLogStatusValues-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.8.6
 */
public class CmsGetLogStatusValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> logReference; /* SEQUENCE OF ObjectReference */

    public CmsGetLogStatusValuesRequest() {
        super(new InnerGetLogStatusValuesRequestPDU());
        this.logReference = new ArrayList<>();
    }

    public CmsGetLogStatusValuesRequest logReference(List<CmsObjectReference> v) {
        this.logReference = v;
        return this;
    }
}
