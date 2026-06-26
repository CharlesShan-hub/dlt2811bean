package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class LdDirDao {

    private String ldName;
    private String referenceAfter;
}
