package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, result [0] IMPLICIT
 * SEQUENCE OF SetMSVCBResult } — 8.10.3
 */
public class CmsSetMsvcbValuesError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsSetMsvcbResult> result; /* SEQUENCE OF SetMSVCBResult */

    public CmsSetMsvcbValuesError() {
        super(Codec.SET_MSVCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.result = new CmsArray<>(CmsSetMsvcbResult.class);
    }

    public CmsSetMsvcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetMsvcbValuesError result(CmsArray<CmsSetMsvcbResult> v) {
        this.result = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, result);
    }
}
