package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * SetDataValues-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *         value         [2] IMPLICIT Data
 *     }
 * } — 8.4.2
 */
public class CmsSetDataValuesRequest extends CmsSequence {

    public List<CmsDataRefValueEntry> data; /* SEQUENCE OF DataRefValueEntry */

    public CmsSetDataValuesRequest() {
        super(new InnerSetDataValuesRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsSetDataValuesRequest data(List<CmsDataRefValueEntry> v) {
        this.data = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerSetDataValuesRequestPDU inner = (InnerSetDataValuesRequestPDU) this.inner;
        inner.data.value.clear();
        for (CmsDataRefValueEntry entry : data) {
            InnerAnonymousSetDataValuesRequestPDUData innerEntry = new InnerAnonymousSetDataValuesRequestPDUData();
            // reference
            entry.reference.syncToInner();
            innerEntry.reference = (InnerObjectReference) entry.reference.inner;
            // fc (optional)
            if (entry.isPresent("fc")) {
                entry.fc.syncToInner();
                innerEntry.fc = (InnerFunctionalConstraint) entry.fc.inner;
                innerEntry._set.add("fc");
            }
            // value
            entry.value.syncToInner();
            innerEntry.value = (InnerData) entry.value.inner;
            inner.data.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerSetDataValuesRequestPDU inner = (InnerSetDataValuesRequestPDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousSetDataValuesRequestPDUData innerEntry : inner.data.value) {
            CmsDataRefValueEntry entry = new CmsDataRefValueEntry();
            // reference
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            // fc (optional)
            if (innerEntry._set.contains("fc")) {
                entry.fc.inner = innerEntry.fc;
                entry.fc.syncFromInner();
                entry.setPresent("fc", true);
            }
            // value
            entry.value.inner = innerEntry.value;
            entry.value.syncFromInner();
            data.add(entry);
        }
    }
}
