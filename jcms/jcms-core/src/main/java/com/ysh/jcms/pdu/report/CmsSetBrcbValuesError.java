package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * SetBRCBValues-ErrorPDU ::= SEQUENCE {
 *     result  [0] IMPLICIT SEQUENCE OF SetBRCBResult
 * } — 8.7.3
 */
public class CmsSetBrcbValuesError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetBrcbResult.class)
    public List<CmsSetBrcbResult> result; /* SEQUENCE OF SetBRCBResult */

    public CmsSetBrcbValuesError() {
        super(new InnerSetBRCBValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetBrcbValuesError result(List<CmsSetBrcbResult> v) {
        this.result = v;
        return this;
    }
}
