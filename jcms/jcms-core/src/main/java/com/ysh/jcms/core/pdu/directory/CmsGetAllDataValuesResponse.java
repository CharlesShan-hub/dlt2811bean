package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataValuesResponsePDU;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.directory.CmsDataValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetAllDataValues-ResponsePDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         value         [1] IMPLICIT Data
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.4
 * }
 * </pre>
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
