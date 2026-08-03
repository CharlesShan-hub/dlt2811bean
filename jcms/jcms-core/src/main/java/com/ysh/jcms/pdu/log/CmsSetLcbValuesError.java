package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;

import java.util.ArrayList;
import java.util.List;

/**
 * SetLCBValues-ErrorPDU ::= SEQUENCE { result [0] IMPLICIT SEQUENCE OF SEQUENCE
 * { error, logEna, datSet, trgOps, intgPd, logRef, optFlds, bufTm } } — 8.8.3
 */
public class CmsSetLcbValuesError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetLcbResult.class)
    public List<CmsSetLcbResult> result; /* SEQUENCE OF SetLCBResult */

    public CmsSetLcbValuesError() {
        super(new InnerSetLCBValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetLcbValuesError result(List<CmsSetLcbResult> v) {
        this.result = v;
        return this;
    }
}
