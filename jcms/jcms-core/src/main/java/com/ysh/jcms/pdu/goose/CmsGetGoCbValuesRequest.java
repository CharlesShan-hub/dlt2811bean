package com.ysh.jcms.pdu.goose;

import com.ysh.jcms.data.InnerGetGoCbValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

import java.util.ArrayList;
import java.util.List;

/**
 * GetGoCbValues-RequestPDU ::= SEQUENCE {
 *     reference   [0] IMPLICIT SEQUENCE OF ObjectReference
 * } — 8.9.4
 */
public class CmsGetGoCbValuesRequest extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsObjectReference.class)
    public List<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetGoCbValuesRequest() {
        super(new InnerGetGoCbValuesRequestPDU());
        this.reference = new ArrayList<>();
    }

    public CmsGetGoCbValuesRequest reference(List<CmsObjectReference> v) { this.reference = v; return this; }
}
