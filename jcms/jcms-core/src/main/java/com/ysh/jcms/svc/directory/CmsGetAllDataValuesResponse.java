package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAnonymousGetAllDataValuesResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataValues-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF DataValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.4
 */
public class CmsGetAllDataValuesResponse extends CmsType {

    public List<CmsDataValueEntry> data; /* SEQUENCE OF DataValueEntry */

    @CmsField
    public boolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataValuesResponse() {
        super(new InnerGetAllDataValuesResponsePDU());
        this.data = new ArrayList<>();
    }

    public CmsGetAllDataValuesResponse data(List<CmsDataValueEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataValuesResponse moreFollows(boolean v) {
        this.moreFollows = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllDataValuesResponsePDU inner = (InnerGetAllDataValuesResponsePDU) this.inner;
        inner.data.value.clear();
        for (CmsDataValueEntry entry : data) {
            InnerAnonymousGetAllDataValuesResponsePDUData innerEntry = new InnerAnonymousGetAllDataValuesResponsePDUData();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            entry.value.syncToInner();
            innerEntry.value = (com.ysh.jcms.data.InnerData) entry.value.inner;
            inner.data.value.add(innerEntry);
        }
        inner.moreFollows.value = moreFollows ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        InnerGetAllDataValuesResponsePDU inner = (InnerGetAllDataValuesResponsePDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousGetAllDataValuesResponsePDUData innerEntry : inner.data.value) {
            CmsDataValueEntry entry = new CmsDataValueEntry();
            entry.reference.inner = innerEntry.reference;
            entry.value.inner = innerEntry.value;
            entry.value.syncFromInner();
            data.add(entry);
        }
        this.moreFollows = inner.moreFollows.value() != 0;
    }
}
