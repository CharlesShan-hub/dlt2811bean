package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsQueryLogAfterResponse() {
        this.reqId       = new CmsReqId();
        this.logEntry    = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logEntry, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeQueryLogAfterResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeQueryLogAfterResponse(nativePtr, data); read(); }
}
