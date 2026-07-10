package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, gocb [0] IMPLICIT
 * SEQUENCE OF SetGoCBEntry } — 8.9.5
 */
public class CmsSetGoCbValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsSetGoCbEntry> gocb; /* SEQUENCE OF SetGoCBEntry */

    public CmsSetGoCbValuesRequest() {
        super(Codec.SET_GO_CB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.gocb = new CmsArray<>(CmsSetGoCbEntry.class);
    }

    public CmsSetGoCbValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetGoCbValuesRequest gocb(CmsArray<CmsSetGoCbEntry> v) {
        this.gocb = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocb);
    }
}
