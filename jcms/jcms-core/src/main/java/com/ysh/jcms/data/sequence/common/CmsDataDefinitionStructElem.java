package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousDataDefinitionStructure;
import com.ysh.jcms.data.scalar.CmsObjectName;
import com.ysh.jcms.data.scalar.CmsFC;

/**
 * DataDefinitionStructElem ::= SEQUENCE { name ObjectName, fc
 * FunctionalConstraint OPTIONAL, type DataDefinition } — 7.7
 * <p>
 * Wraps {@link InnerAnonymousDataDefinitionStructure}.
 */
public class CmsDataDefinitionStructElem extends CmsSequence {

    @CmsField public CmsObjectName name;
    @CmsField(optional = true) public CmsFC fc;
    @CmsField public CmsDataDefinition type;

    public CmsDataDefinitionStructElem() {
        super(new InnerAnonymousDataDefinitionStructure());
    }

    public CmsDataDefinitionStructElem name(String v) { this.name.value(v); return this; }
    public CmsDataDefinitionStructElem fc(int v) { this.fc.value(v); setPresent("fc", true); return this; }
    public CmsDataDefinitionStructElem type(CmsDataDefinition v) { this.type.value(v); return this; }

    public boolean hasFc() { return isPresent("fc"); }
}
