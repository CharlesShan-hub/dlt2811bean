package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     msvcb           [0] IMPLICIT SEQUENCE OF SetMSVCBEntry
 * }  —  8.10.3
 */
public class CmsSetMsvcbValuesRequest extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsSetMsvcbEntry>      msvcb;   /* SEQUENCE OF SetMSVCBEntry */

    public CmsSetMsvcbValuesRequest() {
        this.reqId = new CmsReqId();
        this.msvcb = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsSetMsvcbValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetMsvcbValuesRequest msvcb(CmsArray<CmsSetMsvcbEntry> v) { this.msvcb = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, msvcb);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetMsvcbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetMsvcbValuesRequest(nativePtr, data); read(); }
}