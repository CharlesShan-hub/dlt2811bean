package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcInterfaceDirectoryRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
 *     referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * } — 8.13.2
 */
public class CmsGetRpcInterfaceDirectoryRequest extends CmsSequence {

    @CmsField(optional = true)
    public CmsString referenceAfter;

    public CmsGetRpcInterfaceDirectoryRequest() { super(new InnerGetRpcInterfaceDirectoryRequestPDU()); }

    public CmsGetRpcInterfaceDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetRpcInterfaceDirectoryRequest referenceAfter(byte[] v) { return referenceAfter(v != null ? new String(v) : null); }
}
