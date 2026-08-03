package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerGetDataSetDirectoryResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetDataSetDirectory-ResponsePDU ::= SEQUENCE {
 *     memberData          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference       [0] IMPLICIT ObjectReference,
 *         fc              [1] IMPLICIT FunctionalConstraint
 *     },
 *     moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.5.5
 * }
 * </pre>
 */
public class CmsGetDataSetDirectoryResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsDataRefFcEntry.class)
    public List<CmsDataRefFcEntry> memberData; /* SEQUENCE OF DataRefFcEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataSetDirectoryResponse() {
        super(new InnerGetDataSetDirectoryResponsePDU());
        this.memberData = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataSetDirectoryResponse memberData(List<CmsDataRefFcEntry> v) {
        this.memberData = v;
        return this;
    }
    public CmsGetDataSetDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

}
