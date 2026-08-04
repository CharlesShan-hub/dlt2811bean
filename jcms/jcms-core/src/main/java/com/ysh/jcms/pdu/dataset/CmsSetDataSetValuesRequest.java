package com.ysh.jcms.pdu.dataset;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSetDataSetValuesRequestPDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SetDataSetValues-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     value               [2] IMPLICIT SEQUENCE OF Data
 * } — 8.5.2
 * }
 * </pre>
 */
public class CmsSetDataSetValuesRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> value; /* SEQUENCE OF Data */

    public CmsSetDataSetValuesRequest() {
        super(new InnerSetDataSetValuesRequestPDU());
        this.value = new ArrayList<>();
    }

    public CmsSetDataSetValuesRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest datasetReference(byte[] v) {
        return datasetReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSetDataSetValuesRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetDataSetValuesRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsSetDataSetValuesRequest value(List<CmsData> v) {
        this.value = v;
        return this;
    }

}
