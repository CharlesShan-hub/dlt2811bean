package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerDataDefinitionArray;
import com.ysh.jcms.data.InnerDataDefinition;
import com.ysh.jcms.data.scalar.CmsInt32;

/**
 * DataDefinitionArray ::= SEQUENCE { numberOfElement Int32, elementType
 * DataDefinition } — 7.7
 */
public class CmsDataDefinitionArray extends CmsType {

    public CmsInt32 numberOfElement;
    public CmsDataDefinition elementType;

    public CmsDataDefinitionArray() {
        super(new InnerDataDefinitionArray());
        this.numberOfElement = new CmsInt32();
        this.elementType = new CmsDataDefinition();
    }

    public CmsDataDefinitionArray numberOfElement(int v) { this.numberOfElement.value(v); return this; }
    public CmsDataDefinitionArray elementType(CmsDataDefinition v) { this.elementType = v; return this; }

    @Override
    public void syncToInner() {
        InnerDataDefinitionArray i = (InnerDataDefinitionArray) inner;
        i.numberOfElement.value = numberOfElement.value();
        elementType.syncToInner();
        i.elementType = (InnerDataDefinition) elementType.inner;
    }

    @Override
    public void syncFromInner() {
        InnerDataDefinitionArray i = (InnerDataDefinitionArray) inner;
        numberOfElement.value(i.numberOfElement.value);
        elementType.inner = i.elementType;
        elementType.syncFromInner();
    }
}
