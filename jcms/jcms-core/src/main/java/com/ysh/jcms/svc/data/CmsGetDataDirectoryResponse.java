package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     dataAttribute   [0] IMPLICIT SEQUENCE OF SubRefEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.4.3
 */
public class CmsGetDataDirectoryResponse extends CmsType {

    public CmsReqId                    reqId;
    public CmsArray<CmsSubRefEntry>    dataAttribute;  /* SEQUENCE OF SubRefEntry */
    public CmsBoolean                  moreFollows;    /* DEFAULT TRUE */

    public CmsGetDataDirectoryResponse() {
        this.reqId         = new CmsReqId();
        this.dataAttribute = new CmsArray<>();
        this.moreFollows   = new CmsBoolean();
    }
    
    public CmsGetDataDirectoryResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataDirectoryResponse dataAttribute(CmsArray<CmsSubRefEntry> v) { this.dataAttribute = v; return this; }
    public CmsGetDataDirectoryResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, dataAttribute, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataDirectoryResponse(nativePtr, data); read(); }
}