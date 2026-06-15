package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import java.util.Arrays;
import java.util.List;

/**
 * LogDataEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     fc          [1] IMPLICIT FunctionalConstraint,
 *     value       [2] IMPLICIT Data,
 *     reason      [3] IMPLICIT ReasonCode
 * }  —  8.8.1
 *
 * Used by LogEntry entryData.
 */
public class CmsLogDataEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsFunctionalConstraint fc;
    public CmsData                value;
    public CmsReasonCode          reason;

    public CmsLogDataEntry() {
        this.reference = new CmsObjectReference();
        this.fc        = new CmsFunctionalConstraint();
        this.value     = new CmsData();
        this.reason    = new CmsReasonCode();
    }
    
    // -- chain setters --
    public CmsLogDataEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsLogDataEntry reference(String v) { this.reference.value(v); return this; }
    public CmsLogDataEntry fc(byte[] v) { this.fc.value(v); return this; }
    public CmsLogDataEntry fc(String v) { this.fc.value(v); return this; }
    public CmsLogDataEntry value(CmsData v) { this.value = v; return this; }
    public CmsLogDataEntry reason(CmsReasonCode v) { this.reason = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc, value, reason);
    }
}