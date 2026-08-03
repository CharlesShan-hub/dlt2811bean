package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerQueryLogByTimeResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.log.CmsLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryLogByTime-ResponsePDU ::= SEQUENCE { logEntry [0] IMPLICIT SEQUENCE OF
 * LogEntry, moreFollows [1] IMPLICIT Boolean DEFAULT 1 } — 8.8.4
 */
public class CmsQueryLogByTimeResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsLogEntry.class)
    public List<CmsLogEntry> logEntry; /* SEQUENCE OF LogEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsQueryLogByTimeResponse() {
        super(new InnerQueryLogByTimeResponsePDU());
        this.logEntry = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsQueryLogByTimeResponse logEntry(List<CmsLogEntry> v) {
        this.logEntry = v;
        return this;
    }
    public CmsQueryLogByTimeResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
