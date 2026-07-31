package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerGetRpcMethodDirectoryRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * GetRpcMethodDirectory-RequestPDU ::= SEQUENCE {
 *     interface       [0] IMPLICIT VisibleString OPTIONAL,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * } — 8.13.3
 */
public class CmsGetRpcMethodDirectoryRequest extends CmsSequence {

    @CmsField(optional = true, inner = "interface")
    public CmsString interfaceName; /* ASN.1 "interface" is a Java keyword — mapped via @CmsField.inner */

    @CmsField(optional = true)
    public CmsString referenceAfter;

    public CmsGetRpcMethodDirectoryRequest() { super(new InnerGetRpcMethodDirectoryRequestPDU()); }

    public CmsGetRpcMethodDirectoryRequest interfaceName(String v) {
        if (v != null) {
            this.interfaceName.value(v);
            setPresent("interfaceName", true);
        } else {
            setPresent("interfaceName", false);
        }
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest interfaceName(byte[] v) { return interfaceName(v != null ? new String(v) : null); }
    public CmsGetRpcMethodDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest referenceAfter(byte[] v) { return referenceAfter(v != null ? new String(v) : null); }
}
