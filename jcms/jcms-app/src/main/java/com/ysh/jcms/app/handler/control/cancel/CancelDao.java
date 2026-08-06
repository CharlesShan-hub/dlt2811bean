package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(fluent = true)
public class CancelDao extends BaseDao {
    private String ref;
    private Map<String, String> args;
}
