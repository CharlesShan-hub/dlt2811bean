package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetFile-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255,
 *     startPosition   [1] IMPLICIT INT32U,
 *     fileData        [2] IMPLICIT OCTET STRING,
 *     endOfFile       [3] IMPLICIT BOOLEAN DEFAULT FALSE
 * }  —  8.12.2
 */
public class CmsSetFileRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsUint8Array       filename;
    public CmsInt32U           startPosition;
    public CmsUint8Array       fileData;
    public CmsBoolean          endOfFile;   /* DEFAULT FALSE */

    public CmsSetFileRequest() {
        this.reqId         = new CmsReqId();
        this.filename      = new CmsUint8Array();
        this.startPosition = new CmsInt32U();
        this.fileData      = new CmsUint8Array();
        this.endOfFile     = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsSetFileRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetFileRequest filename(byte[] v) { this.filename.value(v); return this; }
    public CmsSetFileRequest filename(String v) { this.filename.value(v); return this; }
    public CmsSetFileRequest startPosition(long v) { this.startPosition.value(v); return this; }
    public CmsSetFileRequest fileData(byte[] v) { this.fileData.value(v); return this; }
    public CmsSetFileRequest fileData(String v) { this.fileData.value(v); return this; }
    public CmsSetFileRequest endOfFile(boolean v) { this.endOfFile.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename, startPosition, fileData, endOfFile);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetFileRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetFileRequest(nativePtr, data); read(); }
}