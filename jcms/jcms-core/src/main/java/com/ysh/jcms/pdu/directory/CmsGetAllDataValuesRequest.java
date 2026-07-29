package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetAllDataValuesRequestPDU;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.choice.CmsReferenceChoice;

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

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetAllDataValuesRequest() {
        super(new InnerGetAllDataValuesRequestPDU());
    }

    public CmsGetAllDataValuesRequest reference(CmsReferenceChoice v) { this.reference.value(v); return this; }
    public CmsGetAllDataValuesRequest fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsGetAllDataValuesRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
}
