package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataSetValuesDao {
    private String datasetReference;
    private String referenceAfter;
    private List<String> values = new ArrayList<>();

    public SetDataSetValuesDao addValue(String v) {
        values.add(v);
        return this;
    }
}
