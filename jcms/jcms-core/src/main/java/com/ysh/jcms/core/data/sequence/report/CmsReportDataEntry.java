package com.ysh.jcms.core.data.sequence.report;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousReportPDUEntryEntryData;
import com.ysh.jcms.data.bitarray.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * ReportDataEntry ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference OPTIONAL,
 *     fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     id              [2] IMPLICIT INT16U,
 *     value           [3] IMPLICIT Data,
 *     reason          [4] IMPLICIT ReasonCode OPTIONAL
 * } — 8.7.1 (inline within ReportPDU entryData)
 * }
 * </pre>
 */
public class CmsReportDataEntry extends CmsSequence {

    @CmsField(optional = true)
    public CmsObjectReference reference;
    @CmsField(optional = true)
    public CmsFC fc;
    @CmsField
    public CmsInt16U id;
    @CmsField
    public CmsData value;
    @CmsField(optional = true)
    public CmsReasonCode reason;

    public CmsReportDataEntry() {
        super(new InnerAnonymousReportPDUEntryEntryData());
    }

    public CmsReportDataEntry reference(String v) {
        if (v != null) {
            this.reference.value(v);
            setPresent("reference", true);
        } else {
            setPresent("reference", false);
        }
        return this;
    }
    public CmsReportDataEntry reference(byte[] v) {
        return reference(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsReportDataEntry fc(int v) {
        this.fc.value(v);
        setPresent("fc", true);
        return this;
    }
    public CmsReportDataEntry id(int v) {
        this.id.value(v);
        return this;
    }
    public CmsReportDataEntry value(CmsData v) {
        this.value.value(v);
        return this;
    }
    public CmsReportDataEntry reason(CmsReasonCode v) {
        if (v != null) {
            this.reason.value(v);
            setPresent("reason", true);
        } else {
            setPresent("reason", false);
        }
        return this;
    }

    public CmsReportDataEntry value(CmsReportDataEntry v) {
        if (v.isPresent("reference")) {
            this.reference.value(v.reference.value());
            setPresent("reference", true);
        } else {
            setPresent("reference", false);
        }
        if (v.isPresent("fc")) {
            this.fc.value(v.fc.value());
            setPresent("fc", true);
        } else {
            setPresent("fc", false);
        }
        this.id.value(v.id.value());
        this.value.value(v.value);
        if (v.isPresent("reason")) {
            this.reason.value(v.reason);
            setPresent("reason", true);
        } else {
            setPresent("reason", false);
        }
        return this;
    }
}
