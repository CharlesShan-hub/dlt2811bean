package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateDataSet-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     memberData          [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference       [0] IMPLICIT ObjectReference,
 *         fc              [1] IMPLICIT FunctionalConstraint
 *     }
 * } — 8.5.3
 */
public class CmsCreateDataSetRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    @CmsField(optional = true)
    public CmsObjectReference referenceAfter; /* OPTIONAL */

    public List<CmsDataRefFcEntry> memberData; /* SEQUENCE OF DataRefFcEntry */

    public CmsCreateDataSetRequest() {
        super(new InnerCreateDataSetRequestPDU());
        this.datasetReference = new CmsObjectReference();
        this.referenceAfter = new CmsObjectReference();
        this.memberData = new ArrayList<>();
    }

    public CmsCreateDataSetRequest datasetReference(String v) { this.datasetReference.value(v); return this; }
    public CmsCreateDataSetRequest datasetReference(byte[] v) { return datasetReference(new String(v)); }
    public CmsCreateDataSetRequest referenceAfter(byte[] v) {
        return referenceAfter(v != null ? new String(v) : null);
    }
    public CmsCreateDataSetRequest referenceAfter(String v) {
        setPresent("referenceAfter", v != null);
        if (v != null)
            this.referenceAfter.value(v);
        return this;
    }
    public CmsCreateDataSetRequest memberData(List<CmsDataRefFcEntry> v) {
        this.memberData = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerCreateDataSetRequestPDU inner = (InnerCreateDataSetRequestPDU) this.inner;
        inner.memberData.value.clear();
        for (CmsDataRefFcEntry entry : memberData) {
            InnerAnonymousCreateDataSetRequestPDUMemberData innerEntry =
                new InnerAnonymousCreateDataSetRequestPDUMemberData();
            entry.reference.syncToInner();
            innerEntry.reference = (InnerObjectReference) entry.reference.inner;
            entry.fc.syncToInner();
            innerEntry.fc = (InnerFunctionalConstraint) entry.fc.inner;
            inner.memberData.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerCreateDataSetRequestPDU inner = (InnerCreateDataSetRequestPDU) this.inner;
        memberData = new ArrayList<>();
        for (InnerAnonymousCreateDataSetRequestPDUMemberData innerEntry : inner.memberData.value) {
            CmsDataRefFcEntry entry = new CmsDataRefFcEntry();
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            entry.fc.inner = innerEntry.fc;
            entry.fc.syncFromInner();
            memberData.add(entry);
        }
    }
}
