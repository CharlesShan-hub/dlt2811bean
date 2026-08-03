package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerSetGoCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.goose.CmsSetGoCbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetGoCBValues-RequestPDU ::= SEQUENCE { gocb [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { reference [0] IMPLICIT ObjectReference, goEna [1] IMPLICIT Boolean
 * OPTIONAL, goID [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL, datSet
 * [3] IMPLICIT ObjectReference OPTIONAL } } — 8.9.5
 */
public class CmsSetGoCbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetGoCbEntry.class)
    public List<CmsSetGoCbEntry> gocb; /* SEQUENCE OF SetGoCBEntry */

    public CmsSetGoCbValuesRequest() {
        super(new InnerSetGoCBValuesRequestPDU());
        this.gocb = new ArrayList<>();
    }

    public CmsSetGoCbValuesRequest gocb(List<CmsSetGoCbEntry> v) {
        this.gocb = v;
        return this;
    }
}
