package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     fileData        [0] IMPLICIT OCTET STRING,
 *     endOfFile       [1] IMPLICIT BOOLEAN DEFAULT FALSE
 * }  —  8.12.1
 */
public class CmsGetFileResponse extends CmsType {

    public CmsReqId         reqId;
    public CmsUint8Array    fileData;
    public CmsBoolean       endOfFile;   /* DEFAULT FALSE */

    public CmsGetFileResponse() {
        this.reqId     = new CmsReqId();
        this.fileData  = new CmsUint8Array();
        this.endOfFile = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetFileResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetFileResponse fileData(byte[] v) { this.fileData.value(v); return this; }
    public CmsGetFileResponse fileData(String v) { this.fileData.value(v); return this; }
    public CmsGetFileResponse endOfFile(boolean v) { this.endOfFile.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, fileData, endOfFile);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetFileResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetFileResponse(nativePtr, data); read(); }
}