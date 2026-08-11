package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.pdu.log.CmsQueryLogByTimeRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

@Setter
@Getter
@Accessors(fluent = true)
public class QueryLogByTimeDao extends BaseDao {
    private String logRef;
    private Long startTime;
    private Long stopTime;
    private String entryAfter;

    @Override
    public CmsType toRequest() {
        CmsQueryLogByTimeRequest req = new CmsQueryLogByTimeRequest().logReference(logRef);
        if (startTime != null) {
            req.startTime(new CmsBinaryTime().msOfDay(startTime % 86400000L).daysSince1984((int) (startTime / 86400000L)));
        }
        if (stopTime != null) {
            req.stopTime(new CmsBinaryTime().msOfDay(stopTime % 86400000L).daysSince1984((int) (stopTime / 86400000L)));
        }
        if (entryAfter != null) {
            req.entryAfter(entryIdBytes(entryAfter));
        }
        return req;
    }

    /**
     * EntryID 为固定 8 字节 OCTET STRING — 字符串左补 '0' 到 8 字节。
     */
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
}
