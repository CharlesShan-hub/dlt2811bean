package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class QueryLogByTimeDao extends BaseDao {
    private String logRef;
    private Long startTime;
    private Long stopTime;
}
