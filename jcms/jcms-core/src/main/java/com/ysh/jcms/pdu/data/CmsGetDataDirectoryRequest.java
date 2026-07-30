package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.InnerGetDataDirectoryRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * GetDataDirectory-RequestPDU ::= SEQUENCE {
 *     dataReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
 * } — 8.4.3
 */
public class CmsGetDataDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference dataReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetDataDirectoryRequest() {
        super(new InnerGetDataDirectoryRequestPDU());
        this.dataReference = new CmsObjectReference();
        this.referenceAfter = new CmsObjectReference();
    }

    public CmsGetDataDirectoryRequest dataReference(String v) { this.dataReference.value(v); return this; }
    public CmsGetDataDirectoryRequest dataReference(byte[] v) { return dataReference(new String(v)); }
    public CmsGetDataDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsGetDataDirectoryRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
}
