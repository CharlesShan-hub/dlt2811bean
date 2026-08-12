package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.data.InnerGetDataDefinitionResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.data.CmsDataDefResultEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetDataDefinition-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *         definition    [1] IMPLICIT DataDefinition
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.4.4
 * }
 * </pre>
 */
public class CmsGetDataDefinitionResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsDataDefResultEntry.class)
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
