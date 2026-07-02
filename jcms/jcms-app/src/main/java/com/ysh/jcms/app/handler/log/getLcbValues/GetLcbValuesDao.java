package com.ysh.jcms.app.handler.log.getLcbValues;

import java.util.ArrayList;
import java.util.List;

public class GetLcbValuesDao {
    private final List<String> refs = new ArrayList<>();

    public List<String> refs() { return refs; }
    public GetLcbValuesDao addRef(String ref) { refs.add(ref); return this; }
}
