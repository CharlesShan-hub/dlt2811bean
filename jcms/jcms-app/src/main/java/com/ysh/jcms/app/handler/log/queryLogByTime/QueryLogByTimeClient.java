package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeError;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeRequest;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class QueryLogByTimeClient extends BaseClientHandler {

    public QueryLogByTimeClient(CmsNode node) { super(node); }

    public void execute(QueryLogByTimeDao dao) throws Exception {
        CmsQueryLogByTimeRequest req = new CmsQueryLogByTimeRequest()
            .reqId(nextReqId())
            .logReference(dao.logRef());
        if (dao.startTime() != null) {
            req.startTimePresent(true);
            req.startTime.msOfDay(dao.startTime() % 86400000L).daysSince1984((int)(dao.startTime().longValue() / 86400000L));
        }
        if (dao.stopTime() != null) {
            req.stopTimePresent(true);
            req.stopTime.msOfDay(dao.stopTime() % 86400000L).daysSince1984((int)(dao.stopTime().longValue() / 86400000L));
        }
        send(ServiceName.QUERY_LOG_BY_TIME, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogByTimeError err = new CmsQueryLogByTimeError();
        err.decode(frame.asduBytes());
        throw new IOException("QueryLogByTime rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsQueryLogByTimeResponse resp = new CmsQueryLogByTimeResponse();
        resp.logEntry.allocSize = 1024;
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("QueryLogByTime returned {} entries, moreFollows={}",
            resp.logEntry.count, resp.moreFollows.value());
    }
}
