package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeError;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeRequest;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QueryLogByTime — 8.8.4 按时间查询日志服务。
 *
 * <p>当前实现返回空结果（无日志条目），moreFollows=false。
 * 实际日志存储和查询需要持久化层配合。
 */
public class QueryLogByTimeServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryLogByTimeServer.class);

    public QueryLogByTimeServer() {
        super(ServiceName.QUERY_LOG_BY_TIME, CmsQueryLogByTimeRequest.class, CmsQueryLogByTimeError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsQueryLogByTimeRequest req = (CmsQueryLogByTimeRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("QueryLogByTime from {}: reqId={}, logRef={}",
            session.getSessionId(), reqId, str(req.logReference));

        // 返回空结果 — 需要日志持久化层时再实现
        CmsQueryLogByTimeResponse resp = new CmsQueryLogByTimeResponse()
            .reqId(reqId)
            .moreFollows(false);
        resp.logEntry.allocSize = 0;  // 空结果，避免预分配大量嵌套对象

        log.info("QueryLogByTime: returning 0 entries (log storage not yet implemented)");
        return ok(resp, reqId);
    }
}
