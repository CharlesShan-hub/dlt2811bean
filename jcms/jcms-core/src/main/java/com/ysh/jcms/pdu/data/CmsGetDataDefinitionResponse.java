package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.data.CmsDataDefResultEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataDefinition-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *         definition    [1] IMPLICIT DataDefinition
 *     },
 *     moreFollows      [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.4.4
 */
public class CmsGetDataDefinitionResponse extends CmsSequence {

    public List<CmsDataDefResultEntry> data; /* SEQUENCE OF DataDefResultEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataDefinitionResponse() {
        super(new InnerGetDataDefinitionResponsePDU());
        this.data = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataDefinitionResponse data(List<CmsDataDefResultEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }


}
