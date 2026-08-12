package com.ysh.jcms.core.pdu.rpc;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetRpcMethodDirectoryRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * GetRpcMethodDirectory-RequestPDU ::= SEQUENCE {
 *     interface       [0] IMPLICIT VisibleString OPTIONAL,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * } — 8.13.3
 * }
 * </pre>
 */
public class CmsGetRpcMethodDirectoryRequest extends CmsSequence {

    @CmsField(optional = true, inner = "interface")
    public CmsString interfaceName; /* ASN.1 "interface" is a Java keyword — mapped via @CmsField.inner */

    @CmsField(optional = true)
    public CmsString referenceAfter;

    public CmsGetRpcMethodDirectoryRequest() {
        super(new InnerGetRpcMethodDirectoryRequestPDU());
    }

    public CmsGetRpcMethodDirectoryRequest interfaceName(String v) {
        if (v != null) {
            this.interfaceName.value(v);
            setPresent("interfaceName", true);
        } else {
            setPresent("interfaceName", false);
        }
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest interfaceName(byte[] v) {
        return interfaceName(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetRpcMethodDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
}
