package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoCbValuesDao extends BaseDao {
    private final List<String> refs = new ArrayList<>();

    public GetGoCbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }
}
