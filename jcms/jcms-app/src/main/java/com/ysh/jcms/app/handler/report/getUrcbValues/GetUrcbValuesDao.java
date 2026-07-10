package com.ysh.jcms.app.handler.report.getUrcbValues;

import java.util.ArrayList;
import java.util.List;

public class GetUrcbValuesDao {
    private final List<String> refs = new ArrayList<>();

    public List<String> refs() {
        return refs;
    }
    public GetUrcbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }
}
