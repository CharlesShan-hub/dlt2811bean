package com.ysh.jcms.core.pdu.goose;

import com.ysh.jcms.data.InnerSetGoCBValuesErrorPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.sequence.goose.CmsSetGoCbResult;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetGoCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         goEna       [1] IMPLICIT ServiceError OPTIONAL,
 *         goID        [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL
 *     }
 * } — 8.9.5
 * }
 * </pre>
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
