package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerGetURCBValuesRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetURCBValues-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.7.4
 * }
 * </pre>
 */
public class CmsGetUrcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetUrcbValuesRequest() {
        super(new InnerGetURCBValuesRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetUrcbValuesRequest reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
}
