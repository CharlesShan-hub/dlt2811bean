package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsFileEntry;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFileAttributeValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255
 * }  —  8.12.5
 */
public class CmsGetFileAttributeValuesRequest extends CmsType {

    public CmsReqId       reqId;
    public CmsUint8Array  filename;

    public CmsGetFileAttributeValuesRequest() {
        this.reqId    = new CmsReqId();
        this.filename = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetFileAttributeValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetFileAttributeValuesRequest(nativePtr, data); read(); }
}
