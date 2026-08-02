package com.ysh.jcms.pdu.rpc;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetRpcInterfaceDefinitionRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
 *     interface       [0] IMPLICIT VisibleString,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * } — 8.13.4
 */
public class CmsGetRpcInterfaceDefinitionRequest extends CmsSequence {

    @CmsField(inner = "interface")
    public CmsString interfaceName; /* ASN.1 "interface" is a Java keyword — mapped via @CmsField.inner */

    @CmsField(optional = true)
    public CmsString referenceAfter;

    public CmsGetRpcInterfaceDefinitionRequest() { super(new InnerGetRpcInterfaceDefinitionRequestPDU()); }

    public CmsGetRpcInterfaceDefinitionRequest interfaceName(String v) { this.interfaceName.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest interfaceName(byte[] v) { return interfaceName(new String(v, StandardCharsets.UTF_8)); }
    public CmsGetRpcInterfaceDefinitionRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsGetRpcInterfaceDefinitionRequest referenceAfter(byte[] v) { return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null); }
}
