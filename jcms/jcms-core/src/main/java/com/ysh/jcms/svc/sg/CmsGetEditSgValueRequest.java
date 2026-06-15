package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetEditSGValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF SGRefFcEntry
 * }  —  8.6.5
 */
public class CmsGetEditSgValueRequest extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsSgRefFcEntry>      data;   /* SEQUENCE OF SGRefFcEntry */

    public CmsGetEditSgValueRequest() {
        this.reqId = new CmsReqId();
        this.data  = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsGetEditSgValueRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetEditSgValueRequest data(CmsArray<CmsSgRefFcEntry> v) { this.data = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetEditSgValueRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetEditSgValueRequest(nativePtr, data); read(); }
}