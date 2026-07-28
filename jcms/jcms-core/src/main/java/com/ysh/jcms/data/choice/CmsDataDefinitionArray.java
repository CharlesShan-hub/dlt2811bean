package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerDataDefinitionArray;
import com.ysh.jcms.data.scalar.CmsInt32;

/**
 * DataDefinitionArray ::= SEQUENCE { numberOfElement Int32, elementType
 * DataDefinition } — 7.7
 */
public class CmsDataDefinitionArray extends CmsSequence {

    @CmsField public CmsInt32 numberOfElement;
    @CmsField public CmsDataDefinition elementType;

    public CmsDataDefinitionArray() {
        super(new InnerDataDefinitionArray());
    }

    public CmsDataDefinitionArray numberOfElement(int v) { this.numberOfElement.value(v); return this; }
    public CmsDataDefinitionArray elementType(CmsDataDefinition v) { this.elementType = v; return this; }
}
