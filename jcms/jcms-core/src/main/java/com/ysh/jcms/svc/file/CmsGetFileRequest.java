package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
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
    public CmsUint8Array      filename;
    public CmsInt32U          startPosition;

    public CmsGetFileRequest() { super(Codec.GET_FILE_REQUEST);
        this.reqId         = new CmsReqId();
        this.filename      = new CmsUint8Array();
        this.startPosition = new CmsInt32U();
    }
    
    public CmsGetFileRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetFileRequest filename(byte[] v) { this.filename.value(v); return this; }
    public CmsGetFileRequest filename(String v) { this.filename.value(v); return this; }
    public CmsGetFileRequest startPosition(long v) { this.startPosition.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename, startPosition);
    }
}