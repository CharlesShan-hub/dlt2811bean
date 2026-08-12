package com.ysh.jcms.core.pdu.rpc;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
 *     referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * } — 8.13.2
 * }
 * </pre>
 */
public class CmsGetRpcInterfaceDirectoryRequest extends CmsSequence {

    @CmsField(optional = true)
    public CmsString referenceAfter;

    public CmsGetRpcInterfaceDirectoryRequest() {
        super(new InnerGetRpcInterfaceDirectoryRequestPDU());
    }

    public CmsGetRpcInterfaceDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetRpcInterfaceDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
}
