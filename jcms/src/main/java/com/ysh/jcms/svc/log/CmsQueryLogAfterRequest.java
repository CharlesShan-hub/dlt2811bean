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
    public CmsEntryTime        startTime;      /* OPTIONAL */
    public CmsEntryId          entry;

    public CmsQueryLogAfterRequest() {
        this.reqId            = new CmsReqId();
        this.logReference     = new CmsObjectReference();
        this.startTimePresent = new CmsBoolean();
        this.startTime        = new CmsEntryTime();
        this.entry            = new CmsEntryId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logReference, startTimePresent, startTime, entry);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeQueryLogAfterRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeQueryLogAfterRequest(nativePtr, data); read(); }
}
