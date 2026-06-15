package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetGoCBResult
 * }  —  8.9.5
 */
public class CmsSetGoCbValuesError extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsSetGoCbResult>      result;   /* SEQUENCE OF SetGoCBResult */

    public CmsSetGoCbValuesError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsSetGoCbValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetGoCbValuesError result(CmsArray<CmsSetGoCbResult> v) { this.result = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetGoCbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetGoCbValuesError(nativePtr, data); read(); }
}