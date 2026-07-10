package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, lcb [0] IMPLICIT
 * SEQUENCE OF SetLCBEntry } — 8.8.3
 */
public class CmsSetLcbValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsSetLcbEntry> lcb; /* SEQUENCE OF SetLCBEntry */

    public CmsSetLcbValuesRequest() {
        super(Codec.SET_LCB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.lcb = new CmsArray<>(CmsSetLcbEntry.class);
    }

    public CmsSetLcbValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetLcbValuesRequest lcb(CmsArray<CmsSetLcbEntry> v) {
        this.lcb = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, lcb);
    }
}
