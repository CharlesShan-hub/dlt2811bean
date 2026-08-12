package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataDefinitionResponsePDU;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.directory.CmsDataDefinitionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         cdcType       [1] IMPLICIT VisibleString OPTIONAL,
 *         definition    [2] IMPLICIT DataDefinition
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.5
 * }
 * </pre>
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
