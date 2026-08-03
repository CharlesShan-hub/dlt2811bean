package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerSetURCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.report.CmsSetUrcbEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetURCBValues-RequestPDU ::= SEQUENCE { urcb [0] IMPLICIT SEQUENCE OF
 * SetURCBEntry } — 8.7.5
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
