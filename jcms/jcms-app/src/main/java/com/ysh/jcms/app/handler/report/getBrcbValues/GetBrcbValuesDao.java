package com.ysh.jcms.app.handler.report.getBrcbValues;

import java.util.ArrayList;
import java.util.List;

public class GetBrcbValuesDao {
    private final List<String> refs = new ArrayList<>();

    public List<String> refs() { return refs; }
    public GetBrcbValuesDao addRef(String ref) { refs.add(ref); return this; }
}
