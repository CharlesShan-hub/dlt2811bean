package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalNodeDirectoryRequestPDU;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.data.choice.CmsReferenceChoice;

/**
 * GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, acsiClass [1] IMPLICIT ACSIClass, referenceAfter
 * [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.3
 */
public class CmsGetLogicalNodeDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsReferenceChoice reference;

    @CmsField
    public CmsAcsiClass acsiClass;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetLogicalNodeDirectoryRequest() {
        super(new InnerGetLogicalNodeDirectoryRequestPDU());
    }

    public CmsGetLogicalNodeDirectoryRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest referenceAfter(byte[] v) {
        setPresent("referenceAfter", v != null && v.length > 0);
        if (v != null)
            this.referenceAfter.value(new String(v));
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
}
