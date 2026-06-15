package com.ysh.jcms.utils.scl.model.data;

import com.ysh.jcms.data.choice.CmsDataDefinition;

public class SclDataDefinitionEntry {
    public final String ref;
    public final String cdcType;
    public final CmsDataDefinition definition;

    public SclDataDefinitionEntry(String ref, String cdcType, CmsDataDefinition definition) {
        this.ref = ref;
        this.cdcType = cdcType;
        this.definition = definition;
    }
}
