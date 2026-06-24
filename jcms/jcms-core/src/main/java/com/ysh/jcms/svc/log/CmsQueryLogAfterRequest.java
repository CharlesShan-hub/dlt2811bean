package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.time.CmsBinaryTime;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogAfter-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     entry           [2] IMPLICIT EntryID
 * }  —  8.8.5
 */
public class CmsQueryLogAfterRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  logReference;
    public CmsBoolean          startTimePresent;
    public CmsBinaryTime        startTime;      /* OPTIONAL */
    public CmsEntryId          entry;

    public CmsQueryLogAfterRequest() { super(Codec.QUERY_LOG_AFTER_REQUEST);
        this.reqId            = new CmsReqId();
        this.logReference     = new CmsObjectReference();
        this.startTimePresent = new CmsBoolean();
        this.startTime        = new CmsBinaryTime();
        this.entry            = new CmsEntryId();
    }
    
    public CmsQueryLogAfterRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsQueryLogAfterRequest logReference(byte[] v) { this.logReference.value(v); return this; }
    public CmsQueryLogAfterRequest logReference(String v) { this.logReference.value(v); return this; }
    public CmsQueryLogAfterRequest startTimePresent(boolean v) { this.startTimePresent.value(v); return this; }
    public CmsQueryLogAfterRequest startTime(CmsBinaryTime v) { this.startTime = v; return this; }
    public CmsQueryLogAfterRequest entry(byte[] v) { this.entry.value(v); return this; }
    public CmsQueryLogAfterRequest entry(String v) { this.entry.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logReference, startTimePresent, startTime, entry);
    }
}