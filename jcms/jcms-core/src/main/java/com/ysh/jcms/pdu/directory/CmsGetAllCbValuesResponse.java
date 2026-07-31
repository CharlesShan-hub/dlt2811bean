package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllCBValuesResponsePDUCbValue;
import com.ysh.jcms.data.InnerGetAllCBValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetAllCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, cbValue [0] IMPLICIT
 * SEQUENCE OF CBValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.6
 */
public class CmsGetAllCbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsCbValueEntry.class)
    public List<CmsCbValueEntry> cbValue; /* SEQUENCE OF CBValueEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllCbValuesResponse() {
        super(new InnerGetAllCBValuesResponsePDU());
        this.cbValue = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllCbValuesResponse cbValue(List<CmsCbValueEntry> v) {
        this.cbValue = v;
        return this;
    }
    public CmsGetAllCbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }


}
