package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.log.CmsQueryLogAfterError;
import com.ysh.jcms.svc.log.CmsQueryLogAfterRequest;
import com.ysh.jcms.svc.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class QueryLogAfterClient extends BaseClientHandler {

    public QueryLogAfterClient(CmsNode node) {
        super(node);
    }

    public void execute(QueryLogAfterDao dao) throws Exception {
        CmsQueryLogAfterRequest req = new CmsQueryLogAfterRequest().reqId(nextReqId()).logReference(dao.logRef()).entry(dao.entryId());
        if (dao.startTime() != null) {
            req.startTimePresent(true);
            req.startTime.msOfDay(dao.startTime() % 86400000L).daysSince1984((int) (dao.startTime() / 86400000L));
        }
        send(ServiceName.QUERY_LOG_AFTER, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogAfterError err = new CmsQueryLogAfterError();
        err.decode(frame.asduBytes());
        throw new IOException("QueryLogAfter rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsQueryLogAfterResponse resp = new CmsQueryLogAfterResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("QueryLogAfter returned {} entries, moreFollows={}", resp.logEntry.count, resp.moreFollows.value());
    }
}
