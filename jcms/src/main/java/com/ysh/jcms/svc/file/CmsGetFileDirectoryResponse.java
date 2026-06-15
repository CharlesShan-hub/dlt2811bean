package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsFileEntry;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFileDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     fileEntry       [0] IMPLICIT SEQUENCE OF FileEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.12.4
 */
public class CmsGetFileDirectoryResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsFileEntry>         fileEntry;    /* SEQUENCE OF FileEntry */
    public CmsBoolean                     moreFollows;  /* DEFAULT TRUE */

    public CmsGetFileDirectoryResponse() {
        this.reqId       = new CmsReqId();
        this.fileEntry   = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, fileEntry, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetFileDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetFileDirectoryResponse(nativePtr, data); read(); }
}
