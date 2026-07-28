package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogByTime-ResponsePDU ::= SEQUENCE { reqId Int16U, logEntry [0] IMPLICIT
 * SEQUENCE OF LogEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } — 8.8.4
 */
public class CmsQueryLogByTimeResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsLogEntry> logEntry; /* SEQUENCE OF LogEntry */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsQueryLogByTimeResponse() {
        super(Codec.QUERY_LOG_BY_TIME_RESPONSE);
        this.reqId = new CmsReqId();
        this.logEntry = new CmsArray<>(CmsLogEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsQueryLogByTimeResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsQueryLogByTimeResponse logEntry(CmsArray<CmsLogEntry> v) {
        this.logEntry = v;
        return this;
    }
    public CmsQueryLogByTimeResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, logEntry, moreFollows);
    }
}
