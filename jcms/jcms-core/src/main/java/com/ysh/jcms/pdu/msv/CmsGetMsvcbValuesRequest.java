package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerGetMSVCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetMSVCBValues-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.10.2
 * }
 * </pre>
 */
public class CmsGetMsvcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetMsvcbValuesRequest() {
        super(new InnerGetMSVCBValuesRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetMsvcbValuesRequest reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
}
