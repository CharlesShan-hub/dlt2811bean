package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataSetDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     memberData      [0] IMPLICIT SEQUENCE OF DataRefFcEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.5.5
 */
public class CmsGetDataSetDirectoryResponse extends CmsType {

    public CmsReqId                            reqId;
    public CmsArray<CmsDataRefFcEntry>         memberData;   /* SEQUENCE OF DataRefFcEntry */
    public CmsBoolean                          moreFollows;  /* DEFAULT TRUE */

    public CmsGetDataSetDirectoryResponse() {
        this.reqId       = new CmsReqId();
        this.memberData  = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }
    
    // -- chain setters --
    public CmsGetDataSetDirectoryResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataSetDirectoryResponse memberData(CmsArray<CmsDataRefFcEntry> v) { this.memberData = v; return this; }
    public CmsGetDataSetDirectoryResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, memberData, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataSetDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataSetDirectoryResponse(nativePtr, data); read(); }
}