package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAnonymousDataDefinitionStructure;
import com.ysh.jcms.data.InnerFunctionalConstraint;
import com.ysh.jcms.data.InnerObjectName;
import com.ysh.jcms.data.InnerDataDefinition;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.fc.CmsFC;

/**
 * DataDefinitionStructElem ::= SEQUENCE { name ObjectName, fc
 * FunctionalConstraint OPTIONAL, type DataDefinition } — 7.7
 * <p>
 * Wraps {@link InnerAnonymousDataDefinitionStructure}.
 */
public class CmsDataDefinitionStructElem extends CmsType {

    public CmsObjectName name;
    public CmsFC fc; /* OPTIONAL */
    public boolean hasFc;
    public CmsDataDefinition type;

    public CmsDataDefinitionStructElem() {
        super(new InnerAnonymousDataDefinitionStructure());
        this.name = new CmsObjectName();
        this.fc = new CmsFC();
        this.type = new CmsDataDefinition();
    }

    public CmsDataDefinitionStructElem name(String v) { this.name.value(v); return this; }
    public CmsDataDefinitionStructElem fc(int v) { this.fc.value(v); this.hasFc = true; return this; }
    public CmsDataDefinitionStructElem type(CmsDataDefinition v) { this.type = v; return this; }

    @Override
    public void syncToInner() {
        InnerAnonymousDataDefinitionStructure i = (InnerAnonymousDataDefinitionStructure) inner;
        name.syncToInner();
        i.name = (InnerObjectName) name.inner;
        if (hasFc) {
            fc.syncToInner();
            i.fc = (InnerFunctionalConstraint) fc.inner;
            i._set.add("fc");
        }
        type.syncToInner();
        i.type = (InnerDataDefinition) type.inner;
    }

    @Override
    public void syncFromInner() {
        InnerAnonymousDataDefinitionStructure i = (InnerAnonymousDataDefinitionStructure) inner;
        name.inner = i.name;
        name.syncFromInner();
        hasFc = i._set.contains("fc");
        if (hasFc) {
            fc.inner = i.fc;
            fc.syncFromInner();
        }
        type.inner = i.type;
        type.syncFromInner();
    }
}
