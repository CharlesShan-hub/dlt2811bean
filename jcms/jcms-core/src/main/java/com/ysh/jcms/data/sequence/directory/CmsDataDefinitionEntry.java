package com.ysh.jcms.data.sequence.directory;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousGetAllDataDefinitionResponsePDUData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * DataDefinitionEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference,
 * cdcType [1] IMPLICIT VisibleString OPTIONAL, definition [2] IMPLICIT
 * DataDefinition } — 8.3.5
 *
 * Backed by {@link InnerAnonymousGetAllDataDefinitionResponsePDUData}.
 */
public class CmsDataDefinitionEntry extends CmsSequence {

    @CmsField
    public CmsSubReference reference;
    @CmsField(optional = true)
    public CmsString cdcType;
    @CmsField
    public CmsDataDefinition definition;

    public CmsDataDefinitionEntry() {
        super(new InnerAnonymousGetAllDataDefinitionResponsePDUData());
    }

    public CmsDataDefinitionEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsDataDefinitionEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataDefinitionEntry cdcType(byte[] v) {
        return cdcType(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsDataDefinitionEntry cdcType(String v) {
        if (v != null) {
            this.cdcType.value(v);
            setPresent("cdcType", true);
        } else {
            setPresent("cdcType", false);
        }
        return this;
    }
    public CmsDataDefinitionEntry definition(CmsDataDefinition v) {
        this.definition.value(v);
        return this;
    }

    /** Copy all field values from another CmsDataDefinitionEntry (fluent). */
    public CmsDataDefinitionEntry value(CmsDataDefinitionEntry v) {
        reference(v.reference.value());
        if (v.isPresent("cdcType")) {
            this.cdcType.value(v.cdcType.value());
            setPresent("cdcType", true);
        } else {
            setPresent("cdcType", false);
        }
        definition(v.definition);
        return this;
    }
}
