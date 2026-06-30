package com.ysh.jcms.app.handler.data.getDataDirectory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDirectoryDao {
    private String dataReference;
    private String referenceAfter;
}
