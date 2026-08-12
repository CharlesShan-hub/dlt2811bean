package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerQueryLogAfterResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.log.CmsLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * QueryLogAfter-ResponsePDU ::= SEQUENCE {
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.8.5
 * }
 * </pre>
 */
public class CmsQueryLogAfterResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsLogEntry.class)
    public List<CmsLogEntry> logEntry; /* SEQUENCE OF LogEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsQueryLogAfterResponse() {
        super(new InnerQueryLogAfterResponsePDU());
        this.logEntry = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsQueryLogAfterResponse logEntry(List<CmsLogEntry> v) {
        this.logEntry = v;
        return this;
    }
    public CmsQueryLogAfterResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
