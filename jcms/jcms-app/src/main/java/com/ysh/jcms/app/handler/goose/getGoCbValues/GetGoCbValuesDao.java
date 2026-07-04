package com.ysh.jcms.app.handler.goose.getGoCbValues;

import java.util.ArrayList;
import java.util.List;

public class GetGoCbValuesDao {
    private final List<String> refs = new ArrayList<>();

    public List<String> refs() { return refs; }
    public GetGoCbValuesDao addRef(String ref) { refs.add(ref); return this; }
}
