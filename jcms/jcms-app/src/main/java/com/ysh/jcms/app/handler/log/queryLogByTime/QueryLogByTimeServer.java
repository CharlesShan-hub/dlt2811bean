package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.sequence.log.CmsLogEntry;
import com.ysh.jcms.pdu.log.CmsQueryLogByTimeError;
import com.ysh.jcms.pdu.log.CmsQueryLogByTimeRequest;
import com.ysh.jcms.pdu.log.CmsQueryLogByTimeResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.log.LogStorage;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

/**
 * QueryLogByTime — 8.8.4 按时间查询日志服务。
 */
public class QueryLogByTimeServer extends BaseServerHandler<CmsQueryLogByTimeRequest, CmsQueryLogByTimeError> {

    private final LogStorage logStorage;

    public QueryLogByTimeServer() {
        super(ServiceName.QUERY_LOG_BY_TIME, CmsQueryLogByTimeRequest.class, CmsQueryLogByTimeError.class);
        this.logStorage = new LogStorage(CmsConfigLoader.load().protocol().log().rootPath());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsQueryLogByTimeRequest req, int reqId) {
        String logRef = str(req.logReference);

        log.info("QueryLogByTime from {}: reqId={}, logRef={}", session.getSessionId(), reqId, logRef);

        Long startTime = null;
        if (req.isPresent("startTime")) {
            startTime = (long) req.startTime.daysSince1984.value() * 86400000L + req.startTime.msOfDay.value();
        }
        Long stopTime = null;
        if (req.isPresent("stopTime")) {
            stopTime = (long) req.stopTime.daysSince1984.value() * 86400000L + req.stopTime.msOfDay.value();
        }
        String entryAfter = null;
        if (req.isPresent("entryAfter")) {
            entryAfter = str(req.entryAfter.value());
        }

        List<CmsLogEntry> entries = logStorage.queryByTime(logRef, startTime, stopTime, entryAfter, pageSize());

        CmsQueryLogByTimeResponse resp = new CmsQueryLogByTimeResponse();
        for (CmsLogEntry e : entries) {
            resp.logEntry.add(e);
        }
        resp.moreFollows(false);

        log.info("QueryLogByTime: returning {} entries for ref={}", entries.size(), logRef);
        return ok(resp, reqId);
    }
}
