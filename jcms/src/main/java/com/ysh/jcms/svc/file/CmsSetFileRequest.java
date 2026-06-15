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
    public CmsVisibleString255 filename;
    public CmsInt32U           startPosition;
    public CmsUint8Array       fileData;
    public CmsBoolean          endOfFile;   /* DEFAULT FALSE */

    public CmsSetFileRequest() {
        this.reqId         = new CmsReqId();
        this.filename      = new CmsVisibleString255();
        this.startPosition = new CmsInt32U();
        this.fileData      = new CmsUint8Array();
        this.endOfFile     = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, filename, startPosition, fileData, endOfFile);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetFileRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetFileRequest(nativePtr, data); read(); }
}
