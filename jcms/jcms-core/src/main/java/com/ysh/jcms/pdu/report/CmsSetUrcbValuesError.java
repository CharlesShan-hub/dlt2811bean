package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetURCBValuesErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * SetURCBValues-ErrorPDU ::= SEQUENCE { result [0] IMPLICIT SEQUENCE OF
 * SetURCBResult } — 8.7.5
 */
public class CmsSetUrcbValuesError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetUrcbResult.class)
    public List<CmsSetUrcbResult> result; /* SEQUENCE OF SetURCBResult */

    public CmsSetUrcbValuesError() {
        super(new InnerSetURCBValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetUrcbValuesError result(List<CmsSetUrcbResult> v) {
        this.result = v;
        return this;
    }
}
