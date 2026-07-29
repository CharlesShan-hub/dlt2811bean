package com.ysh.jcms.data.sequence.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.sequence.common.CmsSubReference;
import com.ysh.jcms.data.core.CmsString;

/**
 * DataDefinitionEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference,
 * cdcType [1] IMPLICIT VisibleString OPTIONAL, definition [2] IMPLICIT
 * DataDefinition } — 8.3.5
 */
public class CmsDataDefinitionEntry extends CmsSequence {

    @CmsField public CmsSubReference reference;
    @CmsField(optional = true) public CmsString cdcType;
    @CmsField public CmsDataDefinition definition;

    public CmsDataDefinitionEntry() {
        super(new InnerEmpty());
    }

    public CmsDataDefinitionEntry reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsDataDefinitionEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataDefinitionEntry cdcType(byte[] v) { this.cdcType.value(new String(v)); setPresent("cdcType", true); return this; }
    public CmsDataDefinitionEntry cdcType(String v) { this.cdcType.value(v); setPresent("cdcType", true); return this; }
    public CmsDataDefinitionEntry definition(CmsDataDefinition v) { this.definition = v; return this; }
}
