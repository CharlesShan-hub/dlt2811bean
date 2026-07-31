package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerGetBRCBValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * GetBRCBValues-RequestPDU ::= SEQUENCE {
 *     reference   [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.7.2
 */
public class CmsGetBrcbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetBrcbValuesRequest() {
        super(new InnerGetBRCBValuesRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetBrcbValuesRequest reference(List<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }
}
