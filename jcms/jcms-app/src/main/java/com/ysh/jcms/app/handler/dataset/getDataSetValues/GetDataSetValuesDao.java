package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataSetValuesDao {
    private String datasetReference;
    private String referenceAfter;
}
