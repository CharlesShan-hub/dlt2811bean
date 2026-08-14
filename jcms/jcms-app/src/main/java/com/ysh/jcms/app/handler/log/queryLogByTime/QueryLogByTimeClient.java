package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.sequence.log.CmsLogDataEntry;
import com.ysh.jcms.core.data.sequence.log.CmsLogEntry;
import com.ysh.jcms.core.pdu.log.CmsQueryLogByTimeError;
import com.ysh.jcms.core.pdu.log.CmsQueryLogByTimeResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class QueryLogByTimeClient extends BaseClientHandler<QueryLogByTimeDao> {

    public static final class LogEntryItem {
        public final String desc;
        public LogEntryItem(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(QueryLogByTimeDao dao) throws Exception {
        send(CmsServiceInfo.QUERY_LOG_BY_TIME, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsQueryLogByTimeError err = decodeErr(frame, new CmsQueryLogByTimeError());
        throw new IOException("QueryLogByTime rejected: " + err.value());
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
    protected void onSuccess(Frame frame, QueryLogByTimeDao dao) throws IOException {
        CmsQueryLogByTimeResponse resp = decodeResp(frame, new CmsQueryLogByTimeResponse());

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
        log.info("QueryLogByTime returned {} entries, moreFollows={}", resp.logEntry.size(), resp.moreFollows.value());
    }
}
