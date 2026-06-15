package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetEditSGValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF SGRefValueEntry
 * }  —  8.6.3
 */
public class CmsSetEditSgValueRequest extends CmsType {

    public CmsReqId                         reqId;
    public CmsArray<CmsSgRefValueEntry>     data;   /* SEQUENCE OF SGRefValueEntry */

    public CmsSetEditSgValueRequest() {
        this.reqId = new CmsReqId();
        this.data  = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsSetEditSgValueRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetEditSgValueRequest data(CmsArray<CmsSgRefValueEntry> v) { this.data = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetEditSgValueRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetEditSgValueRequest(nativePtr, data); read(); }
}