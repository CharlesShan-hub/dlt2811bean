package com.ysh.jcms.utils.scl.convert;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Data value entry —— contains the reference path, value and bType.
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
