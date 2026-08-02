package com.ysh.jcms.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllCBValuesRequestPDU;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.choice.CmsReferenceChoice;

/**
 * GetAllCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ReferenceChoice, acsiClass [1] IMPLICIT ACSIClass, referenceAfter [2]
 * IMPLICIT ObjectReference OPTIONAL } — 8.3.6
 */
public class CmsGetAllCbValuesRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField
    public CmsAcsiClass acsiClass;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetAllCbValuesRequest() {
        super(new InnerGetAllCBValuesRequestPDU());
    }

    public CmsGetAllCbValuesRequest reference(CmsReferenceChoice v) { this.reference.value(v); return this; }
    public CmsGetAllCbValuesRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetAllCbValuesRequest referenceAfter(byte[] v) {
        if (v != null && v.length > 0) {
            this.referenceAfter.value(new String(v, StandardCharsets.UTF_8));
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetAllCbValuesRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}
