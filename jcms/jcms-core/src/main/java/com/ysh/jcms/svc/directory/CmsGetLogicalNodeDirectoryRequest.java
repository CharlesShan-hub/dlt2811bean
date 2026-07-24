package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, acsiClass [1] IMPLICIT ACSIClass, referenceAfter
 * [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.3
 */
public class CmsGetLogicalNodeDirectoryRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsReferenceChoice reference;
    public CmsAcsiClass acsiClass;
    public CmsBoolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetLogicalNodeDirectoryRequest() {
        super(Codec.GET_LOGICAL_NODE_DIRECTORY_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsReferenceChoice();
        this.acsiClass = new CmsAcsiClass();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetLogicalNodeDirectoryRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest acsiClass(int v) {
        this.acsiClass.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetLogicalNodeDirectoryRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference, acsiClass, refAfterPresent, refAfter);
    }
}
