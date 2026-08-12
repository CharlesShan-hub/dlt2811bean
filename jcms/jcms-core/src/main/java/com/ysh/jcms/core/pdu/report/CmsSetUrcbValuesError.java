package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerSetURCBValuesErrorPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetURCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         rptID       [1] IMPLICIT ServiceError OPTIONAL,
 *         rptEna      [2] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [5] IMPLICIT ServiceError OPTIONAL,
 *         bufTm       [6] IMPLICIT ServiceError OPTIONAL,
 *         trgOps      [8] IMPLICIT ServiceError OPTIONAL,
 *         intgPd      [9] IMPLICIT ServiceError OPTIONAL,
 *         gi          [10] IMPLICIT ServiceError OPTIONAL,
 *         resv        [13] IMPLICIT ServiceError OPTIONAL
 *     }
 * } — 8.7.5
 * }
 * </pre>
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
