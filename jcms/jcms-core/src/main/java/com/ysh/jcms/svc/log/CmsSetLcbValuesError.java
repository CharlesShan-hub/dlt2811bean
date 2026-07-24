package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, result [0] IMPLICIT
 * SEQUENCE OF SetLCBResult } — 8.8.3
 */
public class CmsSetLcbValuesError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsSetLcbResult> result; /* SEQUENCE OF SetLCBResult */

    public CmsSetLcbValuesError() {
        super(Codec.SET_LCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.result = new CmsArray<>(CmsSetLcbResult.class);
    }

    public CmsSetLcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetLcbValuesError result(CmsArray<CmsSetLcbResult> v) {
        this.result = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, result);
    }
}
