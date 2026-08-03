package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSetEditSGValueRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.sg.CmsSgRefValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetEditSGValue-RequestPDU ::= SEQUENCE { data [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { reference [0] IMPLICIT ObjectReference, value [2] IMPLICIT Data }
 * } — 8.6.3
 */
public class CmsSetEditSgValueRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSgRefValueEntry.class)
    public List<CmsSgRefValueEntry> data; /* SEQUENCE OF SGRefValueEntry */

    public CmsSetEditSgValueRequest() {
        super(new InnerSetEditSGValueRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsSetEditSgValueRequest data(List<CmsSgRefValueEntry> v) {
        this.data = v;
        return this;
    }
}
