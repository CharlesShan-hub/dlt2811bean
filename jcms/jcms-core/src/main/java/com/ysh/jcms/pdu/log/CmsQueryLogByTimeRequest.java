package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerQueryLogByTimeRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;

/**
 * QueryLogByTime-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     stopTime        [2] IMPLICIT EntryTime OPTIONAL,
 *     entryAfter      [3] IMPLICIT EntryID OPTIONAL
 * } — 8.8.4
 */
public class CmsQueryLogByTimeRequest extends CmsSequence {

    @CmsField public CmsObjectReference logReference;
    @CmsField(optional = true) public CmsBinaryTime startTime;
    @CmsField(optional = true) public CmsBinaryTime stopTime;
    @CmsField(optional = true) public CmsEntryId entryAfter;

    public CmsQueryLogByTimeRequest() { super(new InnerQueryLogByTimeRequestPDU()); }

    public CmsQueryLogByTimeRequest logReference(byte[] v) { this.logReference.value(new String(v)); return this; }
    public CmsQueryLogByTimeRequest logReference(String v) { this.logReference.value(v); return this; }
    public CmsQueryLogByTimeRequest startTime(CmsBinaryTime v) {
        if (v != null) {
            this.startTime.value(v);
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
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
    public CmsQueryLogByTimeRequest entryAfter(byte[] v) {
        if (v != null) {
            this.entryAfter.value(v);
            setPresent("entryAfter", true);
        } else {
            setPresent("entryAfter", false);
        }
        return this;
    }
}
