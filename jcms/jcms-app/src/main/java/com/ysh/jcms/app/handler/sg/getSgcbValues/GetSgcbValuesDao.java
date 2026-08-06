package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetSgcbValuesDao extends BaseDao {
    private List<String> references = new ArrayList<>();

    public GetSgcbValuesDao addRef(String ref) {
        references.add(ref);
        return this;
    }
}
