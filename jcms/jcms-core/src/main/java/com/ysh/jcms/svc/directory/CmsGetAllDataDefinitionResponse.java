package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAnonymousGetAllDataDefinitionResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataDefinitionResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0]
 * IMPLICIT SEQUENCE OF DataDefinitionEntry, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.5
 */
public class CmsGetAllDataDefinitionResponse extends CmsType {

    public List<CmsDataDefinitionEntry> data; /* SEQUENCE OF DataDefinitionEntry */

    public boolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataDefinitionResponse() {
        super(new InnerGetAllDataDefinitionResponsePDU());
        this.data = new ArrayList<>();
    }

    public CmsGetAllDataDefinitionResponse data(List<CmsDataDefinitionEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllDataDefinitionResponsePDU inner = (InnerGetAllDataDefinitionResponsePDU) this.inner;
        inner.data.value.clear();
        for (CmsDataDefinitionEntry entry : data) {
            InnerAnonymousGetAllDataDefinitionResponsePDUData innerEntry = new InnerAnonymousGetAllDataDefinitionResponsePDUData();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            if (entry.cdcTypePresent) {
                innerEntry.cdcType(new String(entry.cdcType.value()));
            }
            entry.definition.syncToInner();
            innerEntry.definition = (com.ysh.jcms.data.InnerDataDefinition) entry.definition.inner;
            inner.data.value.add(innerEntry);
        }
        inner.moreFollows.value = moreFollows ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        InnerGetAllDataDefinitionResponsePDU inner = (InnerGetAllDataDefinitionResponsePDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousGetAllDataDefinitionResponsePDUData innerEntry : inner.data.value) {
            CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry();
            entry.reference.inner = innerEntry.reference;
            entry.cdcTypePresent = innerEntry._set.contains("cdcType");
            if (entry.cdcTypePresent) {
                entry.cdcType.value(innerEntry.cdcType.getBytes());
            }
            entry.definition.inner = innerEntry.definition;
            entry.definition.syncFromInner();
            data.add(entry);
        }
        this.moreFollows = inner.moreFollows.value() != 0;
    }
}
