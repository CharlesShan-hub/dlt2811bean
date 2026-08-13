package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Data definition entry —— reference + CDC type + CmsDataDefinition structure definition.
 */
@Getter
@Accessors(fluent = true)
public class DataDefinitionEntry {
    private final String ref;
    private final String cdcType;
    private final CmsDataDefinition definition;

    public DataDefinitionEntry(String ref, String cdcType, CmsDataDefinition definition) {
        this.ref = ref;
        this.cdcType = cdcType;
        this.definition = definition;
    }
}
