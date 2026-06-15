package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetLCBResult
 * }  —  8.8.3
 */
public class CmsSetLcbValuesError extends CmsType {

    public CmsReqId                   reqId;
    public CmsArray<CmsSetLcbResult>  result;   /* SEQUENCE OF SetLCBResult */

    public CmsSetLcbValuesError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetLcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetLcbValuesError(nativePtr, data); read(); }
}
