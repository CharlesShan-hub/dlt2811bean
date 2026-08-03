package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetLCBValues-RequestPDU ::= SEQUENCE { lcb [0] IMPLICIT SEQUENCE OF SEQUENCE
 * { reference, logEna, datSet, trgOps, intgPd, logRef, optFlds, bufTm } } —
 * 8.8.3
 */
public class CmsSetLcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetLcbEntry.class)
    public List<CmsSetLcbEntry> lcb; /* SEQUENCE OF SetLCBEntry */

    public CmsSetLcbValuesRequest() {
        super(new InnerSetLCBValuesRequestPDU());
        this.lcb = new ArrayList<>();
    }

    public CmsSetLcbValuesRequest lcb(List<CmsSetLcbEntry> v) {
        this.lcb = v;
        return this;
    }
}
