package com.ysh.jcms.data.sequence.data;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetDataDefinitionResponsePDUData;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * SEQUENCE {
 *     cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *     definition    [1] IMPLICIT DataDefinition
 * } — SEQUENCE OF element — inline within GetDataDefinition-ResponsePDU
 * }
 * </pre>
 *
 * <p>
 * Used by GetDataDefinition Response (SEQUENCE OF DataDefResultEntry).
 */
public class CmsDataDefResultEntry extends CmsSequence {

    @CmsField(optional = true)
    public CmsString cdcType;

    @CmsField
    public CmsDataDefinition definition;

    public CmsDataDefResultEntry() {
        super(new InnerAnonymousGetDataDefinitionResponsePDUData());
    }

    public CmsDataDefResultEntry cdcType(String v) {
        if (v != null) {
            this.cdcType.value(v);
            setPresent("cdcType", true);
        } else {
            setPresent("cdcType", false);
        }
        return this;
    }

    public CmsDataDefResultEntry definition(CmsDataDefinition v) {
        this.definition.value(v);
        return this;
    }

    public CmsDataDefResultEntry value(CmsDataDefResultEntry v) {
        if (v.isPresent("cdcType")) {
            this.cdcType.value(v.cdcType.value());
            setPresent("cdcType", true);
        } else {
            setPresent("cdcType", false);
        }
        this.definition.value(v.definition);
        return this;
    }
}
