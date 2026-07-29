package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllDataValuesResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataValues-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.4
 */
public class CmsGetAllDataValuesResponse extends CmsSequence {

    public List<CmsDataValueEntry> data; /* SEQUENCE OF DataValueEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataValuesResponse() {
        super(new InnerGetAllDataValuesResponsePDU());
        this.data = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllDataValuesResponse data(List<CmsDataValueEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllDataValuesResponsePDU inner = (InnerGetAllDataValuesResponsePDU) this.inner;
        inner.data.value.clear();
        for (CmsDataValueEntry entry : data) {
            InnerAnonymousGetAllDataValuesResponsePDUData innerEntry = new InnerAnonymousGetAllDataValuesResponsePDUData();
            entry.reference.syncToInner();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            entry.value.syncToInner();
            innerEntry.value = (com.ysh.jcms.data.InnerData) entry.value.inner;
            inner.data.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetAllDataValuesResponsePDU inner = (InnerGetAllDataValuesResponsePDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousGetAllDataValuesResponsePDUData innerEntry : inner.data.value) {
            CmsDataValueEntry entry = new CmsDataValueEntry();
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            entry.value.inner = innerEntry.value;
            entry.value.rebindChoices();
            entry.value.syncFromInner();
            data.add(entry);
        }
    }
}
