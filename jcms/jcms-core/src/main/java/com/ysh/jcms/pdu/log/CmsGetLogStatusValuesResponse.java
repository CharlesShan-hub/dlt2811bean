package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLogStatusValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsLogStatusValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetLogStatusValues-ResponsePDU ::= SEQUENCE {
 *     log             [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT SEQUENCE {
 *             oldEntrTm   [0] IMPLICIT EntryTime,
 *             newEntrTm   [1] IMPLICIT EntryTime,
 *             oldEntr     [2] IMPLICIT EntryID,
 *             newEntr     [3] IMPLICIT EntryID
 *         }
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.8.6
 * }
 * </pre>
 */
public class CmsGetLogStatusValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsLogStatusValueChoice.class)
    public List<CmsLogStatusValueChoice> log; /* SEQUENCE OF LogStatusValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogStatusValuesResponse() {
        super(new InnerGetLogStatusValuesResponsePDU());
        this.log = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetLogStatusValuesResponse log(List<CmsLogStatusValueChoice> v) {
        this.log = v;
        return this;
    }
    public CmsGetLogStatusValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
