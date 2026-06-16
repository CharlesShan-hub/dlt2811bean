package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import java.util.Arrays;
import java.util.List;

/**
 * ReportDataEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference OPTIONAL,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     id            [2] IMPLICIT INT16U,
 *     value         [3] IMPLICIT Data,
 *     reason        [4] IMPLICIT ReasonCode OPTIONAL
 * }  —  8.7.1
 *
 * Used by ReportPDU entryData.
 */
public class CmsReportDataEntry extends CmsType {

    public CmsBoolean              refPresent;
    public CmsObjectReference      reference;       /* OPTIONAL */
    public CmsBoolean              fcPresent;
    public CmsFC fc;              /* OPTIONAL */
    public CmsInt16U               id;
    public CmsData                 value;
    public CmsBoolean              reasonPresent;
    public CmsReasonCode           reason;          /* OPTIONAL */

    public CmsReportDataEntry() {
        this.refPresent   = new CmsBoolean();
        this.reference    = new CmsObjectReference();
        this.fcPresent    = new CmsBoolean();
        this.fc           = new CmsFC();
        this.id           = new CmsInt16U();
        this.value        = new CmsData();
        this.reasonPresent = new CmsBoolean();
        this.reason       = new CmsReasonCode();
    }

    public CmsReportDataEntry refPresent(boolean v) { this.refPresent.value(v); return this; }
    public CmsReportDataEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsReportDataEntry reference(String v) { this.reference.value(v); return this; }
    public CmsReportDataEntry fcPresent(boolean v) { this.fcPresent.value(v); return this; }
    public CmsReportDataEntry fc(int v) { this.fcPresent.value(true); this.fc.value(v); return this; }

    public CmsReportDataEntry id(int v) { this.id.value(v); return this; }
    public CmsReportDataEntry value(CmsData v) { this.value = v; return this; }
    public CmsReportDataEntry reasonPresent(boolean v) { this.reasonPresent.value(v); return this; }
    public CmsReportDataEntry reason(CmsReasonCode v) { this.reason = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(refPresent, reference, fcPresent, fc, id, value,
            reasonPresent, reason);
    }
}
