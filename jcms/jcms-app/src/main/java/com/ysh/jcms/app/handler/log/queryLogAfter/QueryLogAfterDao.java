package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.pdu.log.CmsQueryLogAfterRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

@Setter
@Getter
@Accessors(fluent = true)
public class QueryLogAfterDao extends BaseDao {
    private String logRef;
    private String entryId;
    private Long startTime;

    @Override
    public CmsType toRequest() {
        CmsQueryLogAfterRequest req = new CmsQueryLogAfterRequest().logReference(logRef).entry(entryIdBytes(entryId));
        if (startTime != null) {
            req.startTime(new CmsBinaryTime().msOfDay(startTime % 86400000L).daysSince1984((int) (startTime / 86400000L)));
        }
        return req;
    }

    /**
     * EntryID 为固定 8 字节 OCTET STRING — 字符串左补 '0' 到 8 字节（与 MockLogGenerator 的 %08d
     * 格式一致）。
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
