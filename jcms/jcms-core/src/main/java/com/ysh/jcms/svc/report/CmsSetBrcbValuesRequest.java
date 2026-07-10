package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetBRCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, brcb [0] IMPLICIT
 * SEQUENCE OF SetBRCBEntry } — 8.7.3
 */
public class CmsSetBrcbValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsSetBrcbEntry> brcb; /* SEQUENCE OF SetBRCBEntry */

    public CmsSetBrcbValuesRequest() {
        super(Codec.SET_BRCB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.brcb = new CmsArray<>(CmsSetBrcbEntry.class);
    }

    public CmsSetBrcbValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetBrcbValuesRequest brcb(CmsArray<CmsSetBrcbEntry> v) {
        this.brcb = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, brcb);
    }
}
