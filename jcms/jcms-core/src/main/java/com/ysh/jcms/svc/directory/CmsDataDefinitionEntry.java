package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * DataDefinitionEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference,
 * cdcType [1] IMPLICIT VisibleString OPTIONAL, definition [2] IMPLICIT
 * DataDefinition } — 8.3.5
 *
 * Used by GetAllDataDefinition response (SEQUENCE OF DataDefinitionEntry).
 */
public class CmsDataDefinitionEntry extends CmsTypeOld {

    public CmsSubReference reference;
    public CmsBoolean cdcTypePresent;
    public CmsUint8Array cdcType; /* VisibleString OPTIONAL */
    public CmsDataDefinition definition;

    public CmsDataDefinitionEntry() {
        this.reference = new CmsSubReference();
        this.cdcTypePresent = new CmsBoolean();
        this.cdcType = new CmsUint8Array();
        this.definition = new CmsDataDefinition();
    }

    public CmsDataDefinitionEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataDefinitionEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataDefinitionEntry cdcTypePresent(boolean v) {
        this.cdcTypePresent.value(v);
        return this;
    }
    public CmsDataDefinitionEntry cdcType(byte[] v) {
        this.cdcTypePresent.value(v != null && v.length > 0);
        if (v != null)
            this.cdcType.value(v);
        return this;
    }
    public CmsDataDefinitionEntry cdcType(String v) {
        this.cdcTypePresent.value(v != null);
        if (v != null)
            this.cdcType.value(v);
        return this;
    }
    public CmsDataDefinitionEntry definition(CmsDataDefinition v) {
        this.definition = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reference, cdcTypePresent, cdcType, definition);
    }
}
