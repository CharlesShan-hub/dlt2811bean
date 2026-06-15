package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetEditSGValue-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF ServiceError
 * }  —  8.6.3
 */
public class CmsSetEditSgValueError extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsServiceError>      result;   /* SEQUENCE OF ServiceError */

    public CmsSetEditSgValueError() {
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>();
    }
    
    public CmsSetEditSgValueError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetEditSgValueError result(CmsArray<CmsServiceError> v) { this.result = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetEditSgValueError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetEditSgValueError(nativePtr, data); read(); }
}