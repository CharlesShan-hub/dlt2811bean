package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllDataDefinitionResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataDefinitionResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0]
 * IMPLICIT SEQUENCE OF DataDefinitionEntry, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.5
 */
public class CmsGetAllDataDefinitionResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsDataDefinitionEntry.class)
    public List<CmsDataDefinitionEntry> data; /* SEQUENCE OF DataDefinitionEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataDefinitionResponse() {
        super(new InnerGetAllDataDefinitionResponsePDU());
        this.data = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllDataDefinitionResponse data(List<CmsDataDefinitionEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }


}
