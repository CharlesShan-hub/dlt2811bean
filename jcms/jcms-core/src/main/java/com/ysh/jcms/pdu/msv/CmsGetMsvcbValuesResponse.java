package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerGetMSVCBValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsMsvcbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetMSVCBValues-ResponsePDU ::= SEQUENCE {
 *     msvcb           [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT MSVCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.10.2
 * }
 * </pre>
 */
public class CmsGetMsvcbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsMsvcbValueChoice.class)
    public List<CmsMsvcbValueChoice> msvcb; /* SEQUENCE OF MSVCBValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetMsvcbValuesResponse() {
        super(new InnerGetMSVCBValuesResponsePDU());
        this.msvcb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetMsvcbValuesResponse msvcb(List<CmsMsvcbValueChoice> v) {
        this.msvcb = v;
        return this;
    }
    public CmsGetMsvcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}
