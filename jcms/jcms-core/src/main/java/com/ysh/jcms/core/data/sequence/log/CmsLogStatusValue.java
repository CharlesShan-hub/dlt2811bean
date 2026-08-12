package com.ysh.jcms.core.data.sequence.log;

import com.ysh.jcms.data.InnerAnonymousGetLogStatusValuesResponsePDULogValue;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;

/**
 * <pre>
 * {@code
 * (inline SEQUENCE within GetLogStatusValues-ResponsePDU log value) ::= SEQUENCE {
 *     oldEntrTm       [0] IMPLICIT EntryTime,
 *     newEntrTm       [1] IMPLICIT EntryTime,
 *     oldEntr         [2] IMPLICIT EntryID,
 *     newEntr         [3] IMPLICIT EntryID
 * } — 8.8.6
 * }
 * </pre>
 */
public class CmsLogStatusValue extends CmsSequence {

    @CmsField
    public CmsBinaryTime oldEntrTm;
    @CmsField
    public CmsBinaryTime newEntrTm;
    @CmsField
    public CmsEntryId oldEntr;
    @CmsField
    public CmsEntryId newEntr;

    public CmsLogStatusValue() {
        super(new InnerAnonymousGetLogStatusValuesResponsePDULogValue());
    }

    public CmsLogStatusValue oldEntrTm(CmsBinaryTime v) {
        this.oldEntrTm.value(v);
        return this;
    }
    public CmsLogStatusValue newEntrTm(CmsBinaryTime v) {
        this.newEntrTm.value(v);
        return this;
    }
    public CmsLogStatusValue oldEntr(byte[] v) {
        this.oldEntr.value(v);
        return this;
    }
    public CmsLogStatusValue newEntr(byte[] v) {
        this.newEntr.value(v);
        return this;
    }

    /** Copy all field values from another CmsLogStatusValue (fluent). */
    public CmsLogStatusValue value(CmsLogStatusValue v) {
        oldEntrTm(v.oldEntrTm);
        newEntrTm(v.newEntrTm);
        oldEntr(v.oldEntr.value());
        newEntr(v.newEntr.value());
        return this;
    }
}
