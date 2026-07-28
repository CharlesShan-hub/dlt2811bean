package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataValues-RequestPDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataRefEntry } — 8.4.1
 */
public class CmsGetDataValuesRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsDataRefEntry> data; /* SEQUENCE OF DataRefEntry */

    public CmsGetDataValuesRequest() {
        super(Codec.GET_DATA_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.data = new CmsArray<>(CmsDataRefEntry.class);
    }

    public CmsGetDataValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataValuesRequest data(CmsArray<CmsDataRefEntry> v) {
        this.data = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, data);
    }
}
