package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0]
 * IMPLICIT SEQUENCE OF DataDefinitionEntry, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.5
 */
public class CmsGetAllDataDefinitionResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsDataDefinitionEntry> data; /* SEQUENCE OF DataDefinitionEntry */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataDefinitionResponse() {
        super(Codec.GET_ALL_DATA_DEFINITION_RESPONSE);
        this.reqId = new CmsReqId();
        this.data = new CmsArray<>(CmsDataDefinitionEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetAllDataDefinitionResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionResponse data(CmsArray<CmsDataDefinitionEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, data, moreFollows);
    }
}
