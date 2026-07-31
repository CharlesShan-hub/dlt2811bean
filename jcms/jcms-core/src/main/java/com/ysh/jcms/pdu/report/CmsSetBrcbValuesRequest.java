package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetBRCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetBRCBValues-RequestPDU ::= SEQUENCE {
 *     brcb    [0] IMPLICIT SEQUENCE OF SetBRCBEntry
 * } — 8.7.3
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
