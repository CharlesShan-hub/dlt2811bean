package com.ysh.jcms.core.pdu.log;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerQueryLogAfterRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;

/**
 * <pre>
 * {@code
 * QueryLogAfter-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     entry           [2] IMPLICIT EntryID
 * } — 8.8.5
 * }
 * </pre>
 */
public class CmsQueryLogAfterRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference logReference;
    @CmsField(optional = true)
    public CmsBinaryTime startTime;
    @CmsField
    public CmsEntryId entry;

    public CmsQueryLogAfterRequest() {
        super(new InnerQueryLogAfterRequestPDU());
    }

    public CmsQueryLogAfterRequest logReference(byte[] v) {
        this.logReference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsQueryLogAfterRequest logReference(String v) {
        this.logReference.value(v);
        return this;
    }
    public CmsQueryLogAfterRequest startTime(CmsBinaryTime v) {
        if (v != null) {
            this.startTime.value(v);
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
    }
    public CmsQueryLogAfterRequest startTime(Long v) {
        if (v != null) {
            this.startTime.value(new CmsBinaryTime()
                .msOfDay(v % 86400000L)
                .daysSince1984((int) (v / 86400000L)));
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
    }
    public CmsQueryLogAfterRequest entry(byte[] v) {
        this.entry.value(v);
        return this;
    }
    public CmsQueryLogAfterRequest entry(String v) {
        this.entry.value(entryIdBytes(v));
        return this;
    }
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
