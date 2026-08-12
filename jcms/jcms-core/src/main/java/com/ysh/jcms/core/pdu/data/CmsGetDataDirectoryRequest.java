package com.ysh.jcms.core.pdu.data;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetDataDirectoryRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * GetDataDirectory-RequestPDU ::= SEQUENCE {
 *     dataReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
 * } — 8.4.3
 * }
 * </pre>
 */
public class CmsGetDataDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference dataReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetDataDirectoryRequest() {
        super(new InnerGetDataDirectoryRequestPDU());
    }

    public CmsGetDataDirectoryRequest dataReference(String v) {
        this.dataReference.value(v);
        return this;
    }
    public CmsGetDataDirectoryRequest dataReference(byte[] v) {
        return dataReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetDataDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetDataDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}
