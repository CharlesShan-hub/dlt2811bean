package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoCbValues-ResponsePDU ::= SEQUENCE { reqId Int16U, gocb [0] IMPLICIT
 * SEQUENCE OF GoCBValueChoice, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE }
 * — 8.9.4
 */
public class CmsGetGoCbValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsGocbValueChoice> gocb; /* SEQUENCE OF GoCBValueChoice */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetGoCbValuesResponse() {
        super(Codec.GET_GO_CB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
        this.gocb = new CmsArray<>(CmsGocbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetGoCbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetGoCbValuesResponse gocb(CmsArray<CmsGocbValueChoice> v) {
        this.gocb = v;
        return this;
    }
    public CmsGetGoCbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, gocb, moreFollows);
    }
}
