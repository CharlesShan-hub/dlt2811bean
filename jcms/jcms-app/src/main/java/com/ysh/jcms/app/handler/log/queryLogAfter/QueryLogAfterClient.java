package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterError;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class QueryLogAfterClient extends BaseClientHandler<QueryLogAfterDao> {

    @Override
    public void execute(QueryLogAfterDao dao) throws Exception {
        send(ServiceName.QUERY_LOG_AFTER, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogAfterError err = decodeErr(frame, new CmsQueryLogAfterError());
        throw new IOException("QueryLogAfter rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsQueryLogAfterResponse resp = decodeResp(frame, new CmsQueryLogAfterResponse());
        log.info("QueryLogAfter returned {} entries, moreFollows={}", resp.logEntry.size(), resp.moreFollows.value());
    }
}
