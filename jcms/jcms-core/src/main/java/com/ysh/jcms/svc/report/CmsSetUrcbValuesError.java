package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetURCBResult
 * }  —  8.7.5
 */
public class CmsSetUrcbValuesError extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsSetUrcbResult>      result;   /* SEQUENCE OF SetURCBResult */

    public CmsSetUrcbValuesError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>(CmsSetUrcbResult.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetUrcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetUrcbValuesError(nativePtr, data); read(); }
}
