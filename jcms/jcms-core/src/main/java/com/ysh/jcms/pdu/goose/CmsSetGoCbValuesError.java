package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerSetGoCBValuesErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.goose.CmsSetGoCbResult;

import java.util.ArrayList;
import java.util.List;

/**
 * SetGoCBValues-ErrorPDU ::= SEQUENCE { result [0] IMPLICIT SEQUENCE OF
 * SEQUENCE { error [0] IMPLICIT ServiceError OPTIONAL, goEna [1] IMPLICIT
 * ServiceError OPTIONAL, goID [2] IMPLICIT ServiceError OPTIONAL, datSet [3]
 * IMPLICIT ServiceError OPTIONAL } } — 8.9.5
 */
public class CmsSetGoCbValuesError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetGoCbResult.class)
    public List<CmsSetGoCbResult> result; /* SEQUENCE OF SetGoCBResult */

    public CmsSetGoCbValuesError() {
        super(new InnerSetGoCBValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetGoCbValuesError result(List<CmsSetGoCbResult> v) {
        this.result = v;
        return this;
    }
}
