package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsQueryLogAfterError;
import com.ysh.jcms.svc.log.CmsQueryLogAfterRequest;
import com.ysh.jcms.svc.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QueryLogAfter — 8.8.5 查询指定条目之后的日志服务。
 *
 * <p>当前实现返回空结果（无日志条目），moreFollows=false。
 * 实际日志存储和查询需要持久化层配合。
 */
public class QueryLogAfterServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryLogAfterServer.class);

    public QueryLogAfterServer() {
        super(ServiceName.QUERY_LOG_AFTER, CmsQueryLogAfterRequest.class, CmsQueryLogAfterError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsQueryLogAfterRequest req = (CmsQueryLogAfterRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("QueryLogAfter from {}: reqId={}, logRef={}",
            session.getSessionId(), reqId, str(req.logReference));

        CmsQueryLogAfterResponse resp = new CmsQueryLogAfterResponse()
            .reqId(reqId)
            .moreFollows(false);
        resp.logEntry.allocSize = 0;  // 空结果，避免预分配大量嵌套对象

        log.info("QueryLogAfter: returning 0 entries (log storage not yet implemented)");
        return ok(resp, reqId);
    }
}
