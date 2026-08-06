package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetLogStatusValuesDao extends BaseDao {
    private final List<String> refs = new ArrayList<>();

    public GetLogStatusValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }
    public GetLogStatusValuesDao addRefs(String... refs) {
        this.refs.addAll(Arrays.asList(refs));
        return this;
    }
}
