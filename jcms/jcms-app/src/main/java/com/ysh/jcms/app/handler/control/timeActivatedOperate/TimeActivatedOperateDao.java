package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(fluent = true)
public class TimeActivatedOperateDao extends BaseDao {
    private String ref;
    private long operTmEpochSeconds;
    private Map<String, String> args;
}
