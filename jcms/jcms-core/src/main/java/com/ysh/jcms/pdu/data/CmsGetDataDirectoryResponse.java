package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetDataDirectory-ResponsePDU ::= SEQUENCE {
 *     dataAttribute    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *         reference     [0] IMPLICIT SubReference,
 *         fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 *     },
 *     moreFollows      [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.4.3
 */
public class CmsGetDataDirectoryResponse extends CmsSequence {

    public List<CmsSubRefEntry> dataAttribute; /* SEQUENCE OF SubRefEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataDirectoryResponse() {
        super(new InnerGetDataDirectoryResponsePDU());
        this.dataAttribute = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataDirectoryResponse dataAttribute(List<CmsSubRefEntry> v) {
        this.dataAttribute = v;
        return this;
    }
    public CmsGetDataDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetDataDirectoryResponsePDU inner = (InnerGetDataDirectoryResponsePDU) this.inner;
        inner.dataAttribute.value.clear();
        for (CmsSubRefEntry entry : dataAttribute) {
            InnerAnonymousGetDataDirectoryResponsePDUDataAttribute innerEntry =
                new InnerAnonymousGetDataDirectoryResponsePDUDataAttribute();
            // reference
            entry.reference.syncToInner();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            // fc (optional)
            if (entry.isPresent("fc")) {
                entry.fc.syncToInner();
                innerEntry.fc = (InnerFunctionalConstraint) entry.fc.inner;
                innerEntry._set.add("fc");
            }
            inner.dataAttribute.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetDataDirectoryResponsePDU inner = (InnerGetDataDirectoryResponsePDU) this.inner;
        dataAttribute = new ArrayList<>();
        for (InnerAnonymousGetDataDirectoryResponsePDUDataAttribute innerEntry : inner.dataAttribute.value) {
            CmsSubRefEntry entry = new CmsSubRefEntry();
            // reference
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            // fc (optional)
            if (innerEntry._set.contains("fc")) {
                entry.fc.inner = innerEntry.fc;
                entry.fc.syncFromInner();
                entry.setPresent("fc", true);
            }
            dataAttribute.add(entry);
        }
    }
}
