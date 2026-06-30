package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class DeleteDataSetDao {
    private String datasetReference;
}
