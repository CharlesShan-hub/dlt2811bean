package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteFile-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255
 * }  —  8.12.3
 */
public class CmsDeleteFileRequest extends CmsType {

    public CmsReqId       reqId;
    public CmsUint8Array  filename;

    public CmsDeleteFileRequest() {
        this.reqId    = new CmsReqId();
        this.filename = new CmsUint8Array();
    }
    
    // -- chain setters --
    public CmsDeleteFileRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsDeleteFileRequest filename(byte[] v) { this.filename.value(v); return this; }
    public CmsDeleteFileRequest filename(String v) { this.filename.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeDeleteFileRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeDeleteFileRequest(nativePtr, data); read(); }
}