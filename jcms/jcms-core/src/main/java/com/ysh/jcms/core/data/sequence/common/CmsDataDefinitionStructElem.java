package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousDataDefinitionStructure;
import com.ysh.jcms.core.data.scalar.CmsObjectName;
import com.ysh.jcms.core.data.scalar.CmsFC;

/**
 * <pre>
 * {@code
 * DataDefinitionStructElem ::= SEQUENCE {
 *     name             [0] IMPLICIT ObjectName,
 *     fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     type             [2] DataDefinition
 * } — 7.8
 * }
 * </pre>
 *
 * <p>
 * Wraps {@link InnerAnonymousDataDefinitionStructure}.
 */
public class CmsDataDefinitionStructElem extends CmsSequence {

    @CmsField
    public CmsObjectName name;
    @CmsField(optional = true)
    public CmsFC fc;
    @CmsField
    public CmsDataDefinition type;

    public CmsDataDefinitionStructElem() {
        super(new InnerAnonymousDataDefinitionStructure());
    }

    public CmsDataDefinitionStructElem name(String v) {
        this.name.value(v);
        return this;
    }
    public CmsDataDefinitionStructElem fc(int v) {
        this.fc.value(v);
        setPresent("fc", true);
        return this;
    }
    public CmsDataDefinitionStructElem type(CmsDataDefinition v) {
        this.type.value(v);
        return this;
    }

    /** Copy all field values from another CmsDataDefinitionStructElem (fluent). */
    public CmsDataDefinitionStructElem value(CmsDataDefinitionStructElem v) {
        name(v.name.value());
        if (v.isPresent("fc")) {
            this.fc.value(v.fc.value());
            setPresent("fc", true);
        } else {
            setPresent("fc", false);
        }
        type(v.type);
        return this;
    }
}
