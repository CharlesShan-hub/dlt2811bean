package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllDataDefinitionResponsePDUData;
import com.ysh.jcms.data.InnerGetAllDataDefinitionResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * GetAllDataDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, data [0]
 * IMPLICIT SEQUENCE OF DataDefinitionEntry, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.3.5
 */
public class CmsGetAllDataDefinitionResponse extends CmsSequence {

    public List<CmsDataDefinitionEntry> data; /* SEQUENCE OF DataDefinitionEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllDataDefinitionResponse() {
        super(new InnerGetAllDataDefinitionResponsePDU());
        this.data = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllDataDefinitionResponse data(List<CmsDataDefinitionEntry> v) {
        this.data = v;
        return this;
    }
    public CmsGetAllDataDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllDataDefinitionResponsePDU inner = (InnerGetAllDataDefinitionResponsePDU) this.inner;
        inner.data.value.clear();
        for (CmsDataDefinitionEntry entry : data) {
            InnerAnonymousGetAllDataDefinitionResponsePDUData innerEntry = new InnerAnonymousGetAllDataDefinitionResponsePDUData();
            entry.reference.syncToInner();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            if (entry.isPresent("cdcType")) {
                entry.cdcType.syncToInner();
                innerEntry.cdcType = (com.ysh.jcms.data.DefaultInnerVisibleString) entry.cdcType.inner;
            }
            entry.definition.syncToInner();
            innerEntry.definition = (com.ysh.jcms.data.InnerDataDefinition) entry.definition.inner;
            inner.data.value.add(innerEntry);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetAllDataDefinitionResponsePDU inner = (InnerGetAllDataDefinitionResponsePDU) this.inner;
        data = new ArrayList<>();
        for (InnerAnonymousGetAllDataDefinitionResponsePDUData innerEntry : inner.data.value) {
            CmsDataDefinitionEntry entry = new CmsDataDefinitionEntry();
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            boolean cdcTypePresent = innerEntry._set != null && innerEntry._set.contains("cdcType");
            entry.setPresent("cdcType", cdcTypePresent);
            if (cdcTypePresent) {
                entry.cdcType.value(innerEntry.cdcType.value);
            }
            entry.definition.inner = innerEntry.definition;
            entry.definition.rebindChoices();
            entry.definition.syncFromInner();
            data.add(entry);
        }
    }
}
