package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectWithValueDao extends BaseDao {
    private String ref;
    private Map<String, String> args;
}
