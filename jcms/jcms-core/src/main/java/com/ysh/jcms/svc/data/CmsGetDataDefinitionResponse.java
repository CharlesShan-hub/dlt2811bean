package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataDefResultEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.4.4
 */
public class CmsGetDataDefinitionResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsDataDefResultEntry> data; /* SEQUENCE OF DataDefResultEntry */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataDefinitionResponse() {
        super(Codec.GET_DATA_DEFINITION_RESPONSE);
        this.reqId = new CmsReqId();
        this.data = new CmsArray<>(CmsDataDefResultEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetDataDefinitionResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataDefinitionResponse data(CmsArray<CmsDataDefResultEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, data, moreFollows);
    }
}
