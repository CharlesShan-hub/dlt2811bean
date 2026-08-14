package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.sequence.log.CmsLogEntry;
import com.ysh.jcms.core.pdu.log.CmsQueryLogAfterError;
import com.ysh.jcms.core.pdu.log.CmsQueryLogAfterRequest;
import com.ysh.jcms.core.pdu.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.log.LogStorage;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * QueryLogAfter — 8.8.5 查询指定条目之后的日志服务。
 */
public class QueryLogAfterServer extends BaseServerHandler<CmsQueryLogAfterRequest, CmsQueryLogAfterError> {

    private final LogStorage logStorage;

    public QueryLogAfterServer() {
        super(CmsServiceInfo.QUERY_LOG_AFTER, CmsQueryLogAfterRequest.class, CmsQueryLogAfterError.class);
        this.logStorage = new LogStorage(CmsConfigLoader.load().protocol().log().rootPath());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsQueryLogAfterRequest req, int reqId) {
        String logRef = str(req.logReference);

        log.info("QueryLogAfter from {}: reqId={}, logRef={}", session.sessionId(), reqId, logRef);

        String entryId = new String(req.entry.value(), StandardCharsets.UTF_8).trim();
        Long startTime = null;
        if (req.isPresent("startTime")) {
            startTime = (long) req.startTime.daysSince1984.value() * 86400000L + req.startTime.msOfDay.value();
        }

        List<CmsLogEntry> entries = logStorage.queryAfter(logRef, entryId, startTime, pageSize());

        CmsQueryLogAfterResponse resp = new CmsQueryLogAfterResponse();
        for (CmsLogEntry e : entries) {
            resp.logEntry.add(e);
        }
        resp.moreFollows(false);

        log.info("QueryLogAfter: returning {} entries for ref={}", entries.size(), logRef);
        return ok(resp, reqId);
    }
}
