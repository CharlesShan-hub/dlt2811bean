package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.time.CmsBinaryTime;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogByTime-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     stopTime        [2] IMPLICIT EntryTime OPTIONAL,
 *     entryAfter      [3] IMPLICIT EntryID OPTIONAL
 * }  —  8.8.4
 */
public class CmsQueryLogByTimeRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  logReference;
    public CmsBoolean          startTimePresent;
    public CmsBinaryTime        startTime;      /* OPTIONAL */
    public CmsBoolean          stopTimePresent;
    public CmsBinaryTime        stopTime;       /* OPTIONAL */
    public CmsBoolean          entryAfterPresent;
    public CmsEntryId          entryAfter;     /* OPTIONAL */

    public CmsQueryLogByTimeRequest() {
        this.reqId             = new CmsReqId();
        this.logReference      = new CmsObjectReference();
        this.startTimePresent  = new CmsBoolean();
        this.startTime         = new CmsBinaryTime();
        this.stopTimePresent   = new CmsBoolean();
        this.stopTime          = new CmsBinaryTime();
        this.entryAfterPresent = new CmsBoolean();
        this.entryAfter        = new CmsEntryId();
    }
    
    public CmsQueryLogByTimeRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsQueryLogByTimeRequest logReference(byte[] v) { this.logReference.value(v); return this; }
    public CmsQueryLogByTimeRequest logReference(String v) { this.logReference.value(v); return this; }
    public CmsQueryLogByTimeRequest startTimePresent(boolean v) { this.startTimePresent.value(v); return this; }
    public CmsQueryLogByTimeRequest startTime(CmsBinaryTime v) { this.startTime = v; return this; }
    public CmsQueryLogByTimeRequest stopTimePresent(boolean v) { this.stopTimePresent.value(v); return this; }
    public CmsQueryLogByTimeRequest stopTime(CmsBinaryTime v) { this.stopTime = v; return this; }
    public CmsQueryLogByTimeRequest entryAfterPresent(boolean v) { this.entryAfterPresent.value(v); return this; }
    public CmsQueryLogByTimeRequest entryAfter(byte[] v) { this.entryAfterPresent.value(v != null && v.length > 0); if (v != null) this.entryAfter.value(v); return this; }
    public CmsQueryLogByTimeRequest entryAfter(String v) { this.entryAfterPresent.value(v != null); if (v != null) this.entryAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logReference,
            startTimePresent, startTime,
            stopTimePresent, stopTime,
            entryAfterPresent, entryAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeQueryLogByTimeRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeQueryLogByTimeRequest(nativePtr, data); read(); }
}