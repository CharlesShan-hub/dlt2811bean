package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF ServiceError
 * }  —  8.4.2
 */
public class CmsSetDataValuesError extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsServiceError>      result;   /* SEQUENCE OF ServiceError */

    public CmsSetDataValuesError() { super(Codec.SET_DATA_VALUES_ERROR);
        this.reqId  = new CmsReqId();
        this.result = new CmsArray<>(CmsServiceError.class);
    }
    
    public CmsSetDataValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSetDataValuesError result(CmsArray<CmsServiceError> v) { this.result = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }
}