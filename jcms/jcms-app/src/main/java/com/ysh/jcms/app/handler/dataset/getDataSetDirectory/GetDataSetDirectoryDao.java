package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetDirectoryDao {
    private String datasetReference;
    private String referenceAfter;
}
