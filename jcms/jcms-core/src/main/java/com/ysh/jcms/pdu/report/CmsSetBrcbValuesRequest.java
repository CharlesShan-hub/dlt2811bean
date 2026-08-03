package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetBRCBValues-RequestPDU ::= SEQUENCE {
 *     brcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         rptID       [1] IMPLICIT VisibleString129 OPTIONAL,
 *         rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *         datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *         optFlds     [5] IMPLICIT RCBOptFlds OPTIONAL,
 *         bufTm       [6] IMPLICIT INT32U OPTIONAL,
 *         trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *         intgPd      [9] IMPLICIT INT32U OPTIONAL,
 *         gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *         purgeBuf    [11] IMPLICIT BOOLEAN OPTIONAL,
 *         entryID     [12] IMPLICIT EntryID OPTIONAL,
 *         resvTms     [13] IMPLICIT INT16 OPTIONAL
 *     }
 * } — 8.7.3
 * }
 * </pre>
 */
public class CmsSetBrcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSetBrcbEntry.class)
    public List<CmsSetBrcbEntry> brcb; /* SEQUENCE OF SetBRCBEntry */

    public CmsSetBrcbValuesRequest() {
        super(new InnerSetBRCBValuesRequestPDU());
        this.brcb = new ArrayList<>();
    }

    public CmsSetBrcbValuesRequest brcb(List<CmsSetBrcbEntry> v) {
        this.brcb = v;
        return this;
    }
}
