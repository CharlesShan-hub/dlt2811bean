package com.ysh.jcms.app.handler.log.getLogStatusValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetLogStatusValuesDao {
    private final List<String> refs = new ArrayList<>();

    public List<String> refs() { return refs; }
    public GetLogStatusValuesDao addRef(String ref) { refs.add(ref); return this; }
    public GetLogStatusValuesDao refs(String... refs) { this.refs.addAll(Arrays.asList(refs)); return this; }
}
