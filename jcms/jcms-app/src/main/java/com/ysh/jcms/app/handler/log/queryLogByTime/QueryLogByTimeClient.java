package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.log.CmsLogDataEntry;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeError;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeRequest;
import com.ysh.jcms.svc.log.CmsQueryLogByTimeResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class QueryLogByTimeClient extends BaseClientHandler {

    public void execute(QueryLogByTimeDao dao) throws Exception {
        CmsQueryLogByTimeRequest req = new CmsQueryLogByTimeRequest().reqId(nextReqId()).logReference(dao.logRef());
        if (dao.startTime() != null) {
            req.startTimePresent(true);
            req.startTime.msOfDay(dao.startTime() % 86400000L).daysSince1984((int) (dao.startTime().longValue() / 86400000L));
        }
        if (dao.stopTime() != null) {
            req.stopTimePresent(true);
            req.stopTime.msOfDay(dao.stopTime() % 86400000L).daysSince1984((int) (dao.stopTime().longValue() / 86400000L));
        }
        send(ServiceName.QUERY_LOG_BY_TIME, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogByTimeError err = decodeErr(frame, new CmsQueryLogByTimeError());
        throw new IOException("QueryLogByTime rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    private static String str(byte[] data) {
        if (data == null)
            return "";
        int len = 0;
        while (len < data.length && data[len] != 0)
            len++;
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsQueryLogByTimeResponse resp = decodeResp(frame, new CmsQueryLogByTimeResponse());
        ConsolePrinter.list("Log entries (" + resp.logEntry.items.size() + " entries)", resp.logEntry.items, entry -> {
            long epochMs = (long) entry.timeOfEntry.daysSince1984.value() * 86400000L + entry.timeOfEntry.msOfDay.value();
            LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMs), ZoneId.systemDefault());
            String timeStr = dt.toString().replace("T", " ");
            String eid = str(entry.entryId.value());
            StringBuilder sb = new StringBuilder();
            sb.append(timeStr).append("  id=").append(eid);
            for (int j = 0; j < entry.entryData.items.size(); j++) {
                CmsLogDataEntry de = entry.entryData.items.get(j);
                String ref = str(de.reference.value());
                int val = de.value.alt_int32.value();
                sb.append("\n           ").append("\u001B[90m[\u001B[0m").append(j).append("\u001B[90m]\u001B[0m ").append(ref)
                        .append("  value=").append(val);
            }
            return sb.toString();
        });
        log.info("QueryLogByTime returned {} entries, moreFollows={}", resp.logEntry.count, resp.moreFollows.value());
    }
}
