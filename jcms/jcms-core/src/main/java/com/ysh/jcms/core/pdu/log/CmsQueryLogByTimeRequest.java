package com.ysh.jcms.core.pdu.log;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerQueryLogByTimeRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;

/**
 * <pre>
 * {@code
 * QueryLogByTime-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     stopTime        [2] IMPLICIT EntryTime OPTIONAL,
 *     entryAfter      [3] IMPLICIT EntryID OPTIONAL
 * } — 8.8.4
 * }
 * </pre>
 */
public class CmsQueryLogByTimeRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference logReference;
    @CmsField(optional = true)
    public CmsBinaryTime startTime;
    @CmsField(optional = true)
    public CmsBinaryTime stopTime;
    @CmsField(optional = true)
    public CmsEntryId entryAfter;

    public CmsQueryLogByTimeRequest() {
        super(new InnerQueryLogByTimeRequestPDU());
    }

    public CmsQueryLogByTimeRequest logReference(byte[] v) {
        this.logReference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsQueryLogByTimeRequest logReference(String v) {
        this.logReference.value(v);
        return this;
    }
    public CmsQueryLogByTimeRequest startTime(CmsBinaryTime v) {
        if (v != null) {
            this.startTime.value(v);
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
    }
    public CmsQueryLogByTimeRequest startTime(Long v) {
        return startTime(toBinaryTime(v));
    }
    public CmsQueryLogByTimeRequest stopTime(CmsBinaryTime v) {
        if (v != null) {
            this.stopTime.value(v);
            setPresent("stopTime", true);
        } else {
            setPresent("stopTime", false);
        }
        return this;
    }
    public CmsQueryLogByTimeRequest stopTime(Long v) {
        return stopTime(toBinaryTime(v));
    }
    public CmsQueryLogByTimeRequest entryAfter(byte[] v) {
        if (v != null) {
            this.entryAfter.value(v);
            setPresent("entryAfter", true);
        } else {
            setPresent("entryAfter", false);
        }
        return this;
    }
    public CmsQueryLogByTimeRequest entryAfter(String v) {
        return entryAfter(v != null ? entryIdBytes(v) : null);
    }
    private static CmsBinaryTime toBinaryTime(Long v) {
        if (v == null)
            return null;
        return new CmsBinaryTime()
            .msOfDay(v % 86400000L)
            .daysSince1984((int) (v / 86400000L));
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
