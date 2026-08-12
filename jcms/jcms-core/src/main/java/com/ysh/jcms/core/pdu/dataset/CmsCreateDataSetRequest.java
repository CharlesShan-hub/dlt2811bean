package com.ysh.jcms.core.pdu.dataset;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerCreateDataSetRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * CreateDataSet-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     memberData          [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference       [0] IMPLICIT ObjectReference,
 *         fc              [1] IMPLICIT FunctionalConstraint
 *     }
 * } — 8.5.3
 * }
 * </pre>
 */
public class CmsCreateDataSetRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter;

    @CmsField(sequenceOf = true, elementType = CmsDataRefFcEntry.class)
    public List<CmsDataRefFcEntry> memberData; /* SEQUENCE OF DataRefFcEntry */

    public CmsCreateDataSetRequest() {
        super(new InnerCreateDataSetRequestPDU());
        this.memberData = new ArrayList<>();
    }

    public CmsCreateDataSetRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsCreateDataSetRequest datasetReference(byte[] v) {
        return datasetReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsCreateDataSetRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsCreateDataSetRequest referenceAfter(String v) {
        if (v != null) {
            this.referenceAfter.value(v);
            setPresent("referenceAfter", true);
        } else {
            setPresent("referenceAfter", false);
        }
        return this;
    }
    public CmsCreateDataSetRequest memberData(List<CmsDataRefFcEntry> v) {
        this.memberData = v;
        return this;
    }
}
