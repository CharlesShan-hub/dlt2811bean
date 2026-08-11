package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.log.CmsLogDataEntry;
import com.ysh.jcms.data.sequence.log.CmsLogEntry;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterError;
import com.ysh.jcms.pdu.log.CmsQueryLogAfterResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class QueryLogAfterClient extends BaseClientHandler<QueryLogAfterDao> {

    public static final class LogEntryItem {
        public final String desc;
        public LogEntryItem(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(QueryLogAfterDao dao) throws Exception {
        send(ServiceName.QUERY_LOG_AFTER, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogAfterError err = decodeErr(frame, new CmsQueryLogAfterError());
        throw new IOException("QueryLogAfter rejected: " + err.value());
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
    protected void onSuccess(Frame frame, QueryLogAfterDao dao) throws IOException {
        CmsQueryLogAfterResponse resp = decodeResp(frame, new CmsQueryLogAfterResponse());

        List<LogEntryItem> entries = new ArrayList<>();
        for (int i = 0; i < resp.logEntry.size(); i++) {
            CmsLogEntry entry = resp.logEntry.get(i);
            long epochMs = (long) entry.timeOfEntry.daysSince1984.value() * 86400000L + entry.timeOfEntry.msOfDay.value();
            LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMs), ZoneId.systemDefault());
            String timeStr = dt.toString().replace("T", " ");
            String eid = str(entry.entryID.value());
            StringBuilder sb = new StringBuilder();
            sb.append(timeStr).append("  id=").append(eid);
            for (int j = 0; j < entry.entryData.size(); j++) {
                CmsLogDataEntry de = entry.entryData.get(j);
                String ref = de.reference.value();
                int val = de.value.alt_int32.value();
                sb.append(" [").append(j).append("] ").append(ref).append("  value=").append(val);
            }
            entries.add(new LogEntryItem(sb.toString()));
        }
        content().res(entries);
        log.info("QueryLogAfter returned {} entries, moreFollows={}", resp.logEntry.size(), resp.moreFollows.value());
    }
}