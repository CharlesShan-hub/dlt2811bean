package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.ArrayList;
import java.util.List;

/**
 * SetDataSetValues-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     value               [2] IMPLICIT SEQUENCE OF Data
 * } — 8.5.2
 */
public class CmsSetDataSetValuesRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public List<CmsData> value; /* SEQUENCE OF Data */

    public CmsSetDataSetValuesRequest() {
        super(new InnerSetDataSetValuesRequestPDU());
        this.datasetReference = new CmsObjectReference();
        this.referenceAfter = new CmsObjectReference();
        this.value = new ArrayList<>();
    }

    public CmsSetDataSetValuesRequest datasetReference(String v) { this.datasetReference.value(v); return this; }
    public CmsSetDataSetValuesRequest datasetReference(byte[] v) { return datasetReference(new String(v)); }
    public CmsSetDataSetValuesRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsSetDataSetValuesRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest value(List<CmsData> v) {
        this.value = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerSetDataSetValuesRequestPDU inner = (InnerSetDataSetValuesRequestPDU) this.inner;
        inner.value.clear();
        for (CmsData elem : value) {
            elem.syncToInner();
            inner.value.add((InnerData) elem.inner);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerSetDataSetValuesRequestPDU inner = (InnerSetDataSetValuesRequestPDU) this.inner;
        value = new ArrayList<>();
        for (InnerData innerElem : inner.value) {
            CmsData c = new CmsData();
            c.inner = innerElem;
            c.syncFromInner();
            value.add(c);
        }
    }
}
