package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerDataDefinitionArray;
import com.ysh.jcms.core.data.scalar.CmsInt32;

/**
 * <pre>
 * {@code
 * DataDefinitionArray ::= SEQUENCE {
 *     numberOfElement  [1] IMPLICIT Int32,
 *     elementType      [2] DataDefinition
 * } — 7.8
 * }
 * </pre>
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
