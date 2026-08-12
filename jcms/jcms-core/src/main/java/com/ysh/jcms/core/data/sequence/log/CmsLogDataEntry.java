package com.ysh.jcms.core.data.sequence.log;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousLogEntryEntryData;
import com.ysh.jcms.data.bitarray.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * (inline SEQUENCE within LogEntry.entryData) ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint,
 *     value           [2] IMPLICIT Data,
 *     reason          [3] IMPLICIT ReasonCode
 * } — 8.8.1
 * }
 * </pre>
 *
 * <p>
 * Element of {@link CmsLogEntry#entryData}.
 */
public class CmsLogDataEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsFC fc;
    @CmsField
    public CmsData value;
    @CmsField
    public CmsReasonCode reason;

    public CmsLogDataEntry() {
        super(new InnerAnonymousLogEntryEntryData());
    }

    public CmsLogDataEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsLogDataEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsLogDataEntry fc(int v) {
        this.fc.value(v);
        return this;
    }
    public CmsLogDataEntry value(CmsData v) {
        this.value.value(v);
        return this;
    }
    public CmsLogDataEntry reason(CmsReasonCode v) {
        this.reason.value(v);
        return this;
    }

    /** Copy all field values from another CmsLogDataEntry (fluent). */
    public CmsLogDataEntry value(CmsLogDataEntry v) {
        reference(v.reference.value());
        fc(v.fc.value());
        value(v.value);
        reason(v.reason);
        return this;
    }
}
