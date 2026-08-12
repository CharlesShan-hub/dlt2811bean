package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerQueryLogByTimeResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.log.CmsLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * QueryLogByTime-ResponsePDU ::= SEQUENCE {
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.8.4
 * }
 * </pre>
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
