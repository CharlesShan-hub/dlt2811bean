package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataValues-RequestPDU ::= SEQUENCE {
 *     data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT ObjectReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     }
 * } — 8.4.1
 */
public class CmsGetDataValuesRequest extends CmsSequence {

    public List<CmsDataRefEntry> data; /* SEQUENCE OF DataRefEntry */

    public CmsGetDataValuesRequest() {
        super(new InnerGetDataValuesRequestPDU());
        this.data = new ArrayList<>();
    }

    public CmsGetDataValuesRequest data(List<CmsDataRefEntry> v) {
        this.data = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetDataValuesRequestPDU inner = (InnerGetDataValuesRequestPDU) this.inner;
        inner.data.value.clear();
        for (CmsDataRefEntry entry : data) {
            InnerAnonymousGetDataValuesRequestPDUData innerEntry = new InnerAnonymousGetDataValuesRequestPDUData();
            // reference
            entry.reference.syncToInner();
            innerEntry.reference = (InnerObjectReference) entry.reference.inner;
            // fc (optional)
            if (entry.isPresent("fc")) {
                entry.fc.syncToInner();
                innerEntry.fc = (InnerFunctionalConstraint) entry.fc.inner;
                innerEntry._set.add("fc");
            }
            inner.data.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetDataValuesRequestPDU inner = (InnerGetDataValuesRequestPDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousGetDataValuesRequestPDUData innerEntry : inner.data.value) {
            CmsDataRefEntry entry = new CmsDataRefEntry();
            // reference
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            // fc (optional)
            if (innerEntry._set.contains("fc")) {
                entry.fc.inner = innerEntry.fc;
                entry.fc.syncFromInner();
                entry.setPresent("fc", true);
            }
            data.add(entry);
        }
    }
}
