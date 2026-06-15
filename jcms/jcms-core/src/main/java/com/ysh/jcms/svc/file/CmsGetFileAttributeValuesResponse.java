package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsFileEntry;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFileAttributeValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     fileEntry       [0] IMPLICIT FileEntry
 * }  —  8.12.5
 */
public class CmsGetFileAttributeValuesResponse extends CmsType {

    public CmsReqId            reqId;
    public CmsFileEntry        fileEntry;

    public CmsGetFileAttributeValuesResponse() {
        this.reqId     = new CmsReqId();
        this.fileEntry = new CmsFileEntry();
    }
    
    public CmsGetFileAttributeValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetFileAttributeValuesResponse fileEntry(CmsFileEntry v) { this.fileEntry = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, fileEntry);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetFileAttributeValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetFileAttributeValuesResponse(nativePtr, data); read(); }
}