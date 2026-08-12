package com.ysh.jcms.core.pdu.sg;

import com.ysh.jcms.data.InnerSetEditSGValueRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.sequence.sg.CmsSgRefValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetEditSGValue-RequestPDU ::= SEQUENCE {
 *     data    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference   [0] IMPLICIT ObjectReference,
 *         value       [2] IMPLICIT Data
 *     }
 * } — 8.6.3
 * }
 * </pre>
 */
public class CmsSetEditSgValueRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSgRefValueEntry.class)
    public List<CmsSgRefValueEntry> data; /* SEQUENCE OF SGRefValueEntry */

    public CmsSetEditSgValueRequest() {
        super(new InnerSetEditSGValueRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsSetEditSgValueRequest data(List<CmsSgRefValueEntry> v) {
        this.data = v;
        return this;
    }
}
