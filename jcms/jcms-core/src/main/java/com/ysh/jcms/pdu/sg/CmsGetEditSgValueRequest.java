package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerGetEditSGValueRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.sg.CmsSgRefFcEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetEditSGValue-RequestPDU ::= SEQUENCE { data [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1] IMPLICIT
 * FunctionalConstraint } } — 8.6.5
 */
public class CmsGetEditSgValueRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSgRefFcEntry.class)
    public List<CmsSgRefFcEntry> data; /* SEQUENCE OF SGRefFcEntry */

    public CmsGetEditSgValueRequest() {
        super(new InnerGetEditSGValueRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsGetEditSgValueRequest data(List<CmsSgRefFcEntry> v) {
        this.data = v;
        return this;
    }
}
