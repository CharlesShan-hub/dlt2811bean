package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGOOSEElementNumber-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     memberData      [1] IMPLICIT SEQUENCE OF GoRefFcEntry
 * }  —  8.9.3
 */
public class CmsGetGooseElementNumberRequest extends CmsType {

    public CmsReqId                       reqId;
    public CmsObjectReference             gocbReference;
    public CmsArray<CmsGoRefFcEntry>      memberData;   /* SEQUENCE OF GoRefFcEntry */

    public CmsGetGooseElementNumberRequest() {
        this.reqId         = new CmsReqId();
        this.gocbReference = new CmsObjectReference();
        this.memberData    = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsGetGooseElementNumberRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGooseElementNumberRequest gocbReference(byte[] v) { this.gocbReference.value(v); return this; }
    public CmsGetGooseElementNumberRequest gocbReference(String v) { this.gocbReference.value(v); return this; }
    public CmsGetGooseElementNumberRequest memberData(CmsArray<CmsGoRefFcEntry> v) { this.memberData = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocbReference, memberData);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGooseElementNumberRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGooseElementNumberRequest(nativePtr, data); read(); }
}