package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerDataDefinitionArray;
import com.ysh.jcms.data.scalar.CmsInt32;

/**
 * DataDefinitionArray ::= SEQUENCE { numberOfElement Int32, elementType
 * DataDefinition } — 7.7
 */
public class CmsDataDefinitionArray extends CmsSequence {

    @CmsField
    public CmsInt32 numberOfElement;
    @CmsField
    public CmsDataDefinition elementType;

    public CmsDataDefinitionArray() {
        super(new InnerDataDefinitionArray());
    }

    public CmsDataDefinitionArray numberOfElement(int v) {
        this.numberOfElement.value(v);
        return this;
    }
    public CmsDataDefinitionArray elementType(CmsDataDefinition v) {
        this.elementType.value(v);
        return this;
    }

    /** Copy all field values from another CmsDataDefinitionArray (fluent). */
    public CmsDataDefinitionArray value(CmsDataDefinitionArray v) {
        numberOfElement(v.numberOfElement.value());
        elementType(v.elementType);
        return this;
    }
}
