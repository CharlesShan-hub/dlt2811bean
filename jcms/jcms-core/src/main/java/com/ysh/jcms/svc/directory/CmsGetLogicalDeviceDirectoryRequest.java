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

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetLogicalDeviceDirectoryRequest() {
        super(new InnerGetLogicalDeviceDirectoryRequestPDU());
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
    public CmsGetLogicalDeviceDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsGetLogicalDeviceDirectoryRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
}
