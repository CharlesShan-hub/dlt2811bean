package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerSetMSVCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.msv.CmsSetMsvcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetMSVCBValues-RequestPDU ::= SEQUENCE { msvcb [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { reference [0] IMPLICIT ObjectReference, svEna [1] IMPLICIT Boolean
 * OPTIONAL, msvID [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL, datSet
 * [3] IMPLICIT ObjectReference OPTIONAL, smpMod [5] IMPLICIT SmpMod OPTIONAL,
 * smpRate [6] IMPLICIT Int16U OPTIONAL, optFlds [7] IMPLICIT MsvcbOptFlds
 * OPTIONAL } } — 8.10.3
 */
public class CmsSetMsvcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetMsvcbEntry.class)
    public List<CmsSetMsvcbEntry> msvcb; /* SEQUENCE OF SetMSVCBEntry */

    public CmsSetMsvcbValuesRequest() {
        super(new InnerSetMSVCBValuesRequestPDU());
        this.msvcb = new ArrayList<>();
    }

    public CmsSetMsvcbValuesRequest msvcb(List<CmsSetMsvcbEntry> v) {
        this.msvcb = v;
        return this;
    }
}
