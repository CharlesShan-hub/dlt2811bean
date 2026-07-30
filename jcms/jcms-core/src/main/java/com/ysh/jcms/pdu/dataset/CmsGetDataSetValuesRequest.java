package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.InnerGetDataSetValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * GetDataSetValues-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
 * } — 8.5.1
 */
public class CmsGetDataSetValuesRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public CmsGetDataSetValuesRequest() {
        super(new InnerGetDataSetValuesRequestPDU());
        this.datasetReference = new CmsObjectReference();
        this.referenceAfter = new CmsObjectReference();
    }

    public CmsGetDataSetValuesRequest datasetReference(String v) { this.datasetReference.value(v); return this; }
    public CmsGetDataSetValuesRequest datasetReference(byte[] v) { return datasetReference(new String(v)); }
    public CmsGetDataSetValuesRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsGetDataSetValuesRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
}
