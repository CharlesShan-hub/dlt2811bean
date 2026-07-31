package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerQueryLogAfterRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;

/**
 * QueryLogAfter-RequestPDU ::= SEQUENCE {
 *     logReference    [0] IMPLICIT ObjectReference,
 *     startTime       [1] IMPLICIT EntryTime OPTIONAL,
 *     entry           [2] IMPLICIT EntryID
 * } — 8.8.5
 */
public class CmsQueryLogAfterRequest extends CmsSequence {

    @CmsField public CmsObjectReference logReference;
    @CmsField(optional = true) public CmsBinaryTime startTime;
    @CmsField public CmsEntryId entry;

    public CmsQueryLogAfterRequest() { super(new InnerQueryLogAfterRequestPDU()); }

    public CmsQueryLogAfterRequest logReference(byte[] v) { this.logReference.value(new String(v)); return this; }
    public CmsQueryLogAfterRequest logReference(String v) { this.logReference.value(v); return this; }
    public CmsQueryLogAfterRequest startTime(CmsBinaryTime v) {
        if (v != null) {
            this.startTime.value(v);
            setPresent("startTime", true);
        } else {
            setPresent("startTime", false);
        }
        return this;
    }
    public CmsQueryLogAfterRequest entry(byte[] v) { this.entry.value(v); return this; }
}
