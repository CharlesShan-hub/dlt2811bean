package com.ysh.jcms.core.pdu.directory;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllCBValuesResponsePDU;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.sequence.directory.CmsCbValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetAllCBValues-ResponsePDU ::= SEQUENCE {
 *     cbValue          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         value         [1] IMPLICIT CHOICE {
 *             brcb        [0] IMPLICIT BRCB,
 *             urcb        [1] IMPLICIT URCB,
 *             lcb         [2] IMPLICIT LCB,
 *             sgcb        [3] IMPLICIT SGCB,
 *             gocb        [4] IMPLICIT GOCB,
 *             msvcb       [5] IMPLICIT MSVCB
 *         }
 *     },
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.3.6
 * }
 * </pre>
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
