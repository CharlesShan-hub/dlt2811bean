package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterError;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterRequest;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class QueryLogAfterClient extends BaseClientHandler {

    public void execute(QueryLogAfterDao dao) throws Exception {
        CmsQueryLogAfterRequest req = new CmsQueryLogAfterRequest().logReference(dao.logRef()).entry(entryIdBytes(dao.entryId()));
        if (dao.startTime() != null) {
            req.startTime(new CmsBinaryTime().msOfDay(dao.startTime() % 86400000L)
                    .daysSince1984((int) (dao.startTime() / 86400000L)));
        }
        send(ServiceName.QUERY_LOG_AFTER, req);
    }

    /** EntryID 为固定 8 字节 OCTET STRING — 字符串左补 '0' 到 8 字节（与 MockLogGenerator 的 %08d 格式一致）。 */
    private static byte[] entryIdBytes(String id) {
        if (id == null)
            id = "";
        String padded = id;
        while (padded.length() < CmsEntryId.LEN)
            padded = "0" + padded;
        if (padded.length() > CmsEntryId.LEN)
            padded = padded.substring(padded.length() - CmsEntryId.LEN);
        return padded.getBytes(StandardCharsets.UTF_8);
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
