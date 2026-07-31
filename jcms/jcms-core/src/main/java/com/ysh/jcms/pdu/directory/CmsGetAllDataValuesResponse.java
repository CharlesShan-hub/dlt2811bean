package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllDataValuesResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataValues-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.4
 */
public class CmsGetAllDataValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsDataValueEntry.class)
    public List<CmsDataValueEntry> data; /* SEQUENCE OF DataValueEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataValuesResponse() {
        super(new InnerGetAllDataValuesResponsePDU());
        this.data = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllDataValuesResponse data(List<CmsDataValueEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }


}
