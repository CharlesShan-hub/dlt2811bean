package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetBRCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetBRCBResult
 * }  —  8.7.3
 */
public class CmsSetBrcbValuesError extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsSetBrcbResult>      result;   /* SEQUENCE OF SetBRCBResult */

    public CmsSetBrcbValuesError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>(CmsSetBrcbResult.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetBrcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetBrcbValuesError(nativePtr, data); read(); }
}
