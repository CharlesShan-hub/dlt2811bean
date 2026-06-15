package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataRefValueEntry
 * }  —  8.4.2
 */
public class CmsSetDataValuesRequest extends CmsType {

    public CmsReqId                         reqId;
    public CmsArray<CmsDataRefValueEntry>   data;   /* SEQUENCE OF DataRefValueEntry */

    public CmsSetDataValuesRequest() {
        this.reqId = new CmsReqId();
        this.data  = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetDataValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetDataValuesRequest(nativePtr, data); read(); }
}
