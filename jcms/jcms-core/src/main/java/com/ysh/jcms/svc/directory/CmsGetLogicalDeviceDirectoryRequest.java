package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryRequestPDU;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.common.CmsObjectReference;

/**
 * GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, ldName [0]
 * IMPLICIT ObjectName OPTIONAL, referenceAfter [1] IMPLICIT ObjectReference
 * OPTIONAL } — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryRequest extends CmsSequence {

    @CmsField(optional = true)
    public CmsObjectName ldName; /* OPTIONAL */

    @CmsField(inner = "referenceAfter", optional = true)
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetLogicalDeviceDirectoryRequest() {
        super(new InnerGetLogicalDeviceDirectoryRequestPDU());
        this.ldName = new CmsObjectName();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetLogicalDeviceDirectoryRequest ldName(byte[] v) {
        return ldName(v != null ? new String(v) : null);
    }
    public CmsGetLogicalDeviceDirectoryRequest ldName(String v) {
        setPresent("ldName", v != null);
        if (v != null)
            this.ldName.value(v);
        return this;
    }
    public CmsGetLogicalDeviceDirectoryRequest refAfter(byte[] v) {
        return refAfter(v != null ? new String(v) : null);
    }
    public CmsGetLogicalDeviceDirectoryRequest refAfter(String v) {
        setPresent("refAfter", v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
}
