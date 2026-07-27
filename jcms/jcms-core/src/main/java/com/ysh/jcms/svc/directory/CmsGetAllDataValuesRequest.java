package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataValuesRequestPDU;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.svc.other.CmsReferenceChoice;

/**
 * GetAllDataValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, fc [1] IMPLICIT FunctionalConstraint OPTIONAL,
 * referenceAfter [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.4
 */
public class CmsGetAllDataValuesRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField(optional = true)
    public CmsFC fc; /* OPTIONAL */

    @CmsField(inner = "referenceAfter", optional = true)
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetAllDataValuesRequest() {
        super(new InnerGetAllDataValuesRequestPDU());
        InnerGetAllDataValuesRequestPDU pdu = (InnerGetAllDataValuesRequestPDU) this.inner;
        this.reference = new CmsReferenceChoice();
        this.reference.inner = pdu.reference;
        this.fc = new CmsFC();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetAllDataValuesRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetAllDataValuesRequest fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest refAfter(byte[] v) {
        return refAfter(v != null ? new String(v) : null);
    }
    public CmsGetAllDataValuesRequest refAfter(String v) {
        setPresent("refAfter", v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
}
