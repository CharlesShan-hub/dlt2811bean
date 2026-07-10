package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetBRCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, result [0] IMPLICIT
 * SEQUENCE OF SetBRCBResult } — 8.7.3
 */
public class CmsSetBrcbValuesError extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsSetBrcbResult> result; /* SEQUENCE OF SetBRCBResult */

    public CmsSetBrcbValuesError() {
        super(Codec.SET_BRCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.result = new CmsArray<>(CmsSetBrcbResult.class);
    }

    public CmsSetBrcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetBrcbValuesError result(CmsArray<CmsSetBrcbResult> v) {
        this.result = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, result);
    }
}
