package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataRefEntry
 * }  —  8.4.1
 */
public class CmsGetDataValuesRequest extends CmsType {

    public CmsReqId                   reqId;
    public CmsArray<CmsDataRefEntry>  data;   /* SEQUENCE OF DataRefEntry */

    public CmsGetDataValuesRequest() {
        this.reqId = new CmsReqId();
        this.data  = new CmsArray<>(CmsDataRefEntry.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataValuesRequest(nativePtr, data); read(); }
}
