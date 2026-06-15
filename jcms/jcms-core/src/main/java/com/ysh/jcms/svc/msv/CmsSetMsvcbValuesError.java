package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetMSVCBResult
 * }  —  8.10.3
 */
public class CmsSetMsvcbValuesError extends CmsType {

    public CmsReqId                         reqId;
    public CmsArray<CmsSetMsvcbResult>      result;   /* SEQUENCE OF SetMSVCBResult */

    public CmsSetMsvcbValuesError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>();
    }
    
    public CmsSetMsvcbValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetMsvcbValuesError result(CmsArray<CmsSetMsvcbResult> v) { this.result = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetMsvcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetMsvcbValuesError(nativePtr, data); read(); }
}