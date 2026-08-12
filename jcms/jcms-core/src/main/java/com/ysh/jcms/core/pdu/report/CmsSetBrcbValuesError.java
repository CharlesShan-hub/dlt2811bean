package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesErrorPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetBRCBValues-ErrorPDU ::= SEQUENCE {
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
 *         purgeBuf    [11] IMPLICIT ServiceError OPTIONAL,
 *         entryID     [12] IMPLICIT ServiceError OPTIONAL,
 *         resvTms     [14] IMPLICIT ServiceError OPTIONAL
 *     }
 * } — 8.7.3
 * }
 * </pre>
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
