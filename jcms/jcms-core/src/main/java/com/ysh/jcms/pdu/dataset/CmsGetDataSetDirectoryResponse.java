package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * GetDataSetDirectory-ResponsePDU ::= SEQUENCE {
 *     memberData          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference       [0] IMPLICIT ObjectReference,
 *         fc              [1] IMPLICIT FunctionalConstraint
 *     },
 *     moreFollows         [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.5.5
 */
public class CmsGetDataSetDirectoryResponse extends CmsSequence {

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
