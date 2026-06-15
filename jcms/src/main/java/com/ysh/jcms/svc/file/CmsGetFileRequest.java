package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFile-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255,
 *     startPosition   [1] IMPLICIT INT32U
 * }  —  8.12.1
 */
public class CmsGetFileRequest extends CmsType {

    public CmsReqId           reqId;
    public CmsVisibleString255 filename;
    public CmsInt32U          startPosition;

    public CmsGetFileRequest() {
        this.reqId         = new CmsReqId();
        this.filename      = new CmsVisibleString255();
        this.startPosition = new CmsInt32U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename, startPosition);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetFileRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetFileRequest(nativePtr, data); read(); }
}
