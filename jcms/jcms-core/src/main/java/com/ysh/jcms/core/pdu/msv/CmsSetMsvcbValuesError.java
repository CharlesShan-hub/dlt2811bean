package com.ysh.jcms.core.pdu.msv;

import com.ysh.jcms.data.InnerSetMSVCBValuesErrorPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.sequence.msv.CmsSetMsvcbResult;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetMSVCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         svEna       [1] IMPLICIT ServiceError OPTIONAL,
 *         msvID       [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *         smpMod      [5] IMPLICIT ServiceError OPTIONAL,
 *         smpRate     [6] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [7] IMPLICIT ServiceError OPTIONAL
 *     }
 * } — 8.10.3
 * }
 * </pre>
 */
public class CmsSetMsvcbValuesError extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetMsvcbResult.class)
    public List<CmsSetMsvcbResult> result; /* SEQUENCE OF SetMSVCBResult */

    public CmsSetMsvcbValuesError() {
        super(new InnerSetMSVCBValuesErrorPDU());
        this.result = new ArrayList<>();
    }

    public CmsSetMsvcbValuesError result(List<CmsSetMsvcbResult> v) {
        this.result = v;
        return this;
    }
}
