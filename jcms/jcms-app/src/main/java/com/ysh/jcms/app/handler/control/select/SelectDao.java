package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SelectDao extends BaseDao {
    private String ref;
}
