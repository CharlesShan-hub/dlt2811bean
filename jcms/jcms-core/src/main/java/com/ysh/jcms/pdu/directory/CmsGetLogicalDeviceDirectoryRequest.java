package com.ysh.jcms.pdu.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerGetLogicalDeviceDirectoryRequestPDU;
import com.ysh.jcms.data.scalar.CmsObjectName;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, ldName [0]
 * IMPLICIT ObjectName OPTIONAL, referenceAfter [1] IMPLICIT ObjectReference
 * OPTIONAL } — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryRequest extends CmsSequence {

    @CmsField(optional = true)
    public CmsObjectName ldName;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetLogicalDeviceDirectoryRequest() {
        super(new InnerGetLogicalDeviceDirectoryRequestPDU());
    }

    public CmsGetLogicalDeviceDirectoryRequest ldName(byte[] v) {
        return ldName(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetLogicalDeviceDirectoryRequest ldName(String v) {
        if (v != null) {
            this.ldName.value(v);
            setPresent("ldName", true);
        } else {
            setPresent("ldName", false);
        }
        return this;
    }
    public CmsGetLogicalDeviceDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetLogicalDeviceDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}
