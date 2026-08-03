package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerSetDataValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetDataValues-RequestPDU ::= SEQUENCE { data [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1] IMPLICIT
 * FunctionalConstraint OPTIONAL, value [2] IMPLICIT Data } } — 8.4.2
 */
public class CmsSetDataValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsDataRefValueEntry.class)
    public List<CmsDataRefValueEntry> data; /* SEQUENCE OF DataRefValueEntry */

    public CmsSetDataValuesRequest() {
        super(new InnerSetDataValuesRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsSetDataValuesRequest data(List<CmsDataRefValueEntry> v) {
        this.data = v;
        return this;
    }

}
