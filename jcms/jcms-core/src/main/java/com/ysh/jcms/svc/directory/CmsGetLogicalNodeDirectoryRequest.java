package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryRequestPDU;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryRequestPDUReference;
import com.ysh.jcms.data.InnerObjectName;
import com.ysh.jcms.data.InnerObjectReference;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReferenceChoice;

/**
 * GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, acsiClass [1] IMPLICIT ACSIClass, referenceAfter
 * [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.3
 */
public class CmsGetLogicalNodeDirectoryRequest extends CmsType {

    public CmsReferenceChoice reference;
    public CmsAcsiClass acsiClass;
    public boolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetLogicalNodeDirectoryRequest() {
        super(new InnerGetLogicalNodeDirectoryRequestPDU());
        this.reference = new CmsReferenceChoice();
        this.reference.inner = ((InnerGetLogicalNodeDirectoryRequestPDU) this.inner).reference;
        this.acsiClass = new CmsAcsiClass();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetLogicalNodeDirectoryRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest refAfter(byte[] v) {
        this.refAfterPresent = v != null && v.length > 0;
        if (v != null)
            this.refAfter.value(new String(v));
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest refAfter(String v) {
        this.refAfterPresent = v != null;
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetLogicalNodeDirectoryRequestPDU inner = (InnerGetLogicalNodeDirectoryRequestPDU) this.inner;
        InnerGetLogicalNodeDirectoryRequestPDUReference innerRef = inner.reference;
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
        InnerGetLogicalNodeDirectoryRequestPDU inner = (InnerGetLogicalNodeDirectoryRequestPDU) this.inner;
        InnerGetLogicalNodeDirectoryRequestPDUReference innerRef = inner.reference;
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
