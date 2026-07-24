package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataValues-RequestPDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataRefValueEntry } — 8.4.2
 */
public class CmsSetDataValuesRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsDataRefValueEntry> data; /* SEQUENCE OF DataRefValueEntry */

    public CmsSetDataValuesRequest() {
        super(Codec.SET_DATA_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.data = new CmsArray<>(CmsDataRefValueEntry.class);
    }

    public CmsSetDataValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetDataValuesRequest data(CmsArray<CmsDataRefValueEntry> v) {
        this.data = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, data);
    }
}
