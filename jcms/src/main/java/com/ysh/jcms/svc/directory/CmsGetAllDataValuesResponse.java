package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataValueEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.3.4
 */
public class CmsGetAllDataValuesResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsDataValueEntry>    data;          /* SEQUENCE OF DataValueEntry */
    public CmsBoolean                     moreFollows;   /* DEFAULT TRUE */

    public CmsGetAllDataValuesResponse() {
        this.reqId       = new CmsReqId();
        this.data        = new CmsArray<>(CmsDataValueEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetAllDataValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetAllDataValuesResponse(nativePtr, data); read(); }
}
