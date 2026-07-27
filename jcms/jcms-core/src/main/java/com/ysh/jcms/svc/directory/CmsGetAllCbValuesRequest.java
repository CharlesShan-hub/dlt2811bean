package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetAllCBValuesRequestPDU;
import com.ysh.jcms.data.InnerGetAllCBValuesRequestPDUReference;
import com.ysh.jcms.data.InnerObjectName;
import com.ysh.jcms.data.InnerObjectReference;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReferenceChoice;

/**
 * GetAllCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ReferenceChoice, acsiClass [1] IMPLICIT ACSIClass, referenceAfter [2]
 * IMPLICIT ObjectReference OPTIONAL } — 8.3.6
 */
public class CmsGetAllCbValuesRequest extends CmsType {

    public CmsReferenceChoice reference;
    public CmsAcsiClass acsiClass;
    public boolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetAllCbValuesRequest() {
        super(new InnerGetAllCBValuesRequestPDU());
        InnerGetAllCBValuesRequestPDU pdu = (InnerGetAllCBValuesRequestPDU) this.inner;
        this.reference = new CmsReferenceChoice();
        this.reference.inner = pdu.reference;
        this.acsiClass = new CmsAcsiClass();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetAllCbValuesRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetAllCbValuesRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetAllCbValuesRequest refAfter(byte[] v) {
        this.refAfterPresent = v != null && v.length > 0;
        if (v != null)
            this.refAfter.value(new String(v));
        return this;
    }
    public CmsGetAllCbValuesRequest refAfter(String v) {
        this.refAfterPresent = v != null;
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllCBValuesRequestPDU inner = (InnerGetAllCBValuesRequestPDU) this.inner;
        InnerGetAllCBValuesRequestPDUReference innerRef = inner.reference;
        int choice = reference.choice.value();
        if (choice == CmsReferenceChoice.LD_NAME && reference.altLdName != null) {
            innerRef._choice = "ldName";
            innerRef.ldName = (InnerObjectName) reference.altLdName.inner;
        } else if (reference.altLnReference != null) {
            innerRef._choice = "lnReference";
            innerRef.lnReference = (InnerObjectReference) reference.altLnReference.inner;
        }
        inner.acsiClass.value = acsiClass.value();
        if (refAfterPresent && refAfter != null) {
            inner.referenceAfter((InnerObjectReference) refAfter.inner);
        }
    }

    @Override
    public void syncFromInner() {
        InnerGetAllCBValuesRequestPDU inner = (InnerGetAllCBValuesRequestPDU) this.inner;
        InnerGetAllCBValuesRequestPDUReference innerRef = inner.reference;
        if ("ldName".equals(innerRef._choice)) {
            reference.choice.value(CmsReferenceChoice.LD_NAME);
            reference.altLdName.inner = innerRef.ldName;
        } else {
            reference.choice.value(CmsReferenceChoice.LN_REFERENCE);
            reference.altLnReference.inner = innerRef.lnReference;
        }
        this.acsiClass.value(inner.acsiClass.value);
        this.refAfter.inner = inner.referenceAfter;
        this.refAfterPresent = !"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".equals(inner.referenceAfter.value);
    }
}
