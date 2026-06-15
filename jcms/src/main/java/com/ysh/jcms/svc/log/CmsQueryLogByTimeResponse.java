package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogByTime-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.8.4
 */
public class CmsQueryLogByTimeResponse extends CmsType {

    public CmsReqId                    reqId;
    public CmsArray<CmsLogEntry>       logEntry;     /* SEQUENCE OF LogEntry */
    public CmsBoolean                  moreFollows;  /* DEFAULT TRUE */

    public CmsQueryLogByTimeResponse() {
        this.reqId       = new CmsReqId();
        this.logEntry    = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logEntry, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeQueryLogByTimeResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeQueryLogByTimeResponse(nativePtr, data); read(); }
}
