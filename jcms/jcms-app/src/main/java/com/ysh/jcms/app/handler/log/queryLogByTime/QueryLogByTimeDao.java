package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.pdu.log.CmsQueryLogByTimeRequest;
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

    @Override
    public CmsType toRequest() {
        CmsQueryLogByTimeRequest req = new CmsQueryLogByTimeRequest().logReference(logRef);
        if (startTime != null) {
            req.startTime(new CmsBinaryTime().msOfDay(startTime % 86400000L).daysSince1984((int) (startTime / 86400000L)));
        }
        if (stopTime != null) {
            req.stopTime(new CmsBinaryTime().msOfDay(stopTime % 86400000L).daysSince1984((int) (stopTime / 86400000L)));
        }
        return req;
    }
}
