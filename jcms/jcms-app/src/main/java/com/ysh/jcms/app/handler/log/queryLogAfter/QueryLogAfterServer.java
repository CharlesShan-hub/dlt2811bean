package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.svc.log.CmsLogEntry;
import com.ysh.jcms.svc.log.CmsQueryLogAfterError;
import com.ysh.jcms.svc.log.CmsQueryLogAfterRequest;
import com.ysh.jcms.svc.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.log.LogStorage;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * QueryLogAfter — 8.8.5 查询指定条目之后的日志服务。
 */
public class QueryLogAfterServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryLogAfterServer.class);

    private final LogStorage logStorage;

    public QueryLogAfterServer() {
        super(ServiceName.QUERY_LOG_AFTER, CmsQueryLogAfterRequest.class, CmsQueryLogAfterError.class);
        this.logStorage = new LogStorage(CmsConfigLoader.load().getProtocol().getLog().getRootPath());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsQueryLogAfterRequest req = (CmsQueryLogAfterRequest) rawReq;
        int reqId = req.reqId.value();
        String logRef = str(req.logReference);

        log.info("QueryLogAfter from {}: reqId={}, logRef={}", session.getSessionId(), reqId, logRef);

        String entryId = new String(req.entry.value(), StandardCharsets.UTF_8).trim();
        Long startTime = null;
        if (req.startTimePresent.value()) {
            startTime = (long) req.startTime.daysSince1984.value() * 86400000L + req.startTime.msOfDay.value();
        }

        List<CmsLogEntry> entries = logStorage.queryAfter(logRef, entryId, startTime, pageSize());

        CmsQueryLogAfterResponse resp = new CmsQueryLogAfterResponse().reqId(reqId);
        for (CmsLogEntry e : entries) {
            resp.logEntry.add(e);
        }
        resp.moreFollows(false);

        log.info("QueryLogAfter: returning {} entries for ref={}", entries.size(), logRef);
        return ok(resp, reqId);
    }
}
