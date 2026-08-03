package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerSetLCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetLCBValues-RequestPDU ::= SEQUENCE {
 *     lcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         logEna      [1] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [2] IMPLICIT ObjectReference OPTIONAL,
 *         trgOps      [3] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [4] IMPLICIT INT32U OPTIONAL,
 *         logRef      [5] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [6] IMPLICIT LCBOptFlds OPTIONAL,
 *         bufTm       [7] IMPLICIT INT32U OPTIONAL
 *     }
 * } — 8.8.3
 * }
 * </pre>
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
