package com.ysh.jcms.utils.scl2.convert;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 数据值条目 —— 包含引用路径、值和 bType。
 */
@Getter
@Accessors(fluent = true)
public class DataValueEntry {
    private final String ref;
    private final String val;
    private final String bType;

    public DataValueEntry(String ref, String val, String bType) {
        this.ref = ref;
        this.val = val;
        this.bType = bType;
    }
}
