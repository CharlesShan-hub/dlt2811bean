package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogAfter-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.8.5
 */
public class CmsQueryLogAfterResponse extends CmsType {

    public CmsReqId                    reqId;
    public CmsArray<CmsLogEntry>       logEntry;     /* SEQUENCE OF LogEntry */
    public CmsBoolean                  moreFollows;  /* DEFAULT TRUE */

    public CmsQueryLogAfterResponse() { super(Codec.QUERY_LOG_AFTER_RESPONSE);
        this.reqId       = new CmsReqId();
        this.logEntry    = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsQueryLogAfterResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsQueryLogAfterResponse logEntry(CmsArray<CmsLogEntry> v) { this.logEntry = v; return this; }
    public CmsQueryLogAfterResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logEntry, moreFollows);
    }
}