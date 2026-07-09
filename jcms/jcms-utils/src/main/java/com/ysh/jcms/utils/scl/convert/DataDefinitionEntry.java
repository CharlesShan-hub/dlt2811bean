package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 数据定义条目 —— 引用 + CDC 类型 + CmsDataDefinition 结构定义。
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
