package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetLCBValues-ErrorPDU ::= SEQUENCE {
 *     result          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         error       [0] IMPLICIT ServiceError OPTIONAL,
 *         logEna      [1] IMPLICIT ServiceError OPTIONAL,
 *         datSet      [2] IMPLICIT ServiceError OPTIONAL,
 *         trgOps      [3] IMPLICIT ServiceError OPTIONAL,
 *         intgPd      [4] IMPLICIT ServiceError OPTIONAL,
 *         logRef      [5] IMPLICIT ServiceError OPTIONAL,
 *         optFlds     [6] IMPLICIT ServiceError OPTIONAL,
 *         bufTm       [7] IMPLICIT ServiceError OPTIONAL
 *     }
 * } — 8.8.3
 * }
 * </pre>
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
