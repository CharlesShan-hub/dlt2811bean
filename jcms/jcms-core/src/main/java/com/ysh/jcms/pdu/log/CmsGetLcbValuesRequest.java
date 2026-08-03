package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetLCBValues-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.8.2
 * }
 * </pre>
 */
public class CmsGetLcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetLcbValuesRequest() {
        super(new InnerGetLCBValuesRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetLcbValuesRequest reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
}
