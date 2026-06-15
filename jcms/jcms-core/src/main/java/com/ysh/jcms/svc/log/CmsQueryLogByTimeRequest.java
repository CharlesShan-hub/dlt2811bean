package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.common.CmsEntryTime;
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
    public CmsEntryTime        startTime;      /* OPTIONAL */
    public CmsBoolean          stopTimePresent;
    public CmsEntryTime        stopTime;       /* OPTIONAL */
    public CmsBoolean          entryAfterPresent;
    public CmsEntryId          entryAfter;     /* OPTIONAL */

    public CmsQueryLogByTimeRequest() {
        this.reqId             = new CmsReqId();
        this.logReference      = new CmsObjectReference();
        this.startTimePresent  = new CmsBoolean();
        this.startTime         = new CmsEntryTime();
        this.stopTimePresent   = new CmsBoolean();
        this.stopTime          = new CmsEntryTime();
        this.entryAfterPresent = new CmsBoolean();
        this.entryAfter        = new CmsEntryId();
    }

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
