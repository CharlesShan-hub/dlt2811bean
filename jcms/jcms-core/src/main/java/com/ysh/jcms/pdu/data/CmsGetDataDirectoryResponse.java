package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataDirectory-ResponsePDU ::= SEQUENCE {
 *     dataAttribute    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     },
 *     moreFollows      [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.4.3
 */
public class CmsGetDataDirectoryResponse extends CmsSequence {

    public List<CmsSubRefEntry> dataAttribute; /* SEQUENCE OF SubRefEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataDirectoryResponse() {
        super(new InnerGetDataDirectoryResponsePDU());
        this.dataAttribute = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataDirectoryResponse dataAttribute(List<CmsSubRefEntry> v) {
        this.dataAttribute = v;
        return this;
    }
    public CmsGetDataDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }


}
