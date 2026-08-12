package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerSetURCBValuesRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.sequence.report.CmsSetUrcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetURCBValues-RequestPDU ::= SEQUENCE {
 *     urcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         rptID       [1] IMPLICIT VisibleString129 OPTIONAL,
 *         rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [5] IMPLICIT RCBOptFlds OPTIONAL,
 *         bufTm       [6] IMPLICIT INT32U OPTIONAL,
 *         trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [9] IMPLICIT INT32U OPTIONAL,
 *         gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *         resv        [13] IMPLICIT BOOLEAN OPTIONAL
 *     }
 * } — 8.7.5
 * }
 * </pre>
 */
public class CmsSetUrcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetUrcbEntry.class)
    public List<CmsSetUrcbEntry> urcb; /* SEQUENCE OF SetURCBEntry */

    public CmsSetUrcbValuesRequest() {
        super(new InnerSetURCBValuesRequestPDU());
        this.urcb = new ArrayList<>();
    }

    public CmsSetUrcbValuesRequest urcb(List<CmsSetUrcbEntry> v) {
        this.urcb = v;
        return this;
    }
}
