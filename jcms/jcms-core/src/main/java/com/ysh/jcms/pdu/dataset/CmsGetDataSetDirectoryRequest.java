package com.ysh.jcms.pdu.dataset;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerGetDataSetDirectoryRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * GetDataSetDirectory-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
 * } — 8.5.5
 * }
 * </pre>
 */
public class CmsGetDataSetDirectoryRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    public CmsGetDataSetDirectoryRequest() {
        super(new InnerGetDataSetDirectoryRequestPDU());
        this.datasetReference = new CmsObjectReference();
        this.referenceAfter = new CmsObjectReference();
    }

    public CmsGetDataSetDirectoryRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest datasetReference(byte[] v) {
        return datasetReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGetDataSetDirectoryRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsGetDataSetDirectoryRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
}
