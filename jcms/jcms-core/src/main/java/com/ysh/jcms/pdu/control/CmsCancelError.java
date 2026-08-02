package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerCancelErrorPDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsAddCause;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsReqId;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * Cancel-ErrorPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, ctlVal
 * [1] IMPLICIT Data, operTm [2] IMPLICIT TimeStamp OPTIONAL, origin [3]
 * IMPLICIT Originator, ctlNum [4] IMPLICIT Int8U, t [5] IMPLICIT TimeStamp,
 * test [6] IMPLICIT Boolean, addCause [8] IMPLICIT AddCause } — 8.11.4
 *
 * reqId is handled at the protocol level (not part of the PDU body).
 */
public class CmsCancelError extends CmsSequence {

    public CmsReqId reqId;

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField(optional = true) public CmsUtcTime operTm;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;
    @CmsField public CmsAddCause addCause;

    public CmsCancelError() {
        super(new InnerCancelErrorPDU());
        this.reqId = new CmsReqId();
    }

    public CmsCancelError reqId(int v) { this.reqId.value(v); return this; }
    public CmsCancelError reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsCancelError reference(String v) { this.reference.value(v); return this; }
    public CmsCancelError ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsCancelError operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsCancelError origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsCancelError ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsCancelError t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsCancelError test(boolean v) { this.test.value(v); return this; }
    public CmsCancelError addCause(int v) { this.addCause.value(v); return this; }

    public CmsCancelError value(CmsCancelError v) {
        reqId(v.reqId.value());
        reference(v.reference.value());
        ctlVal(v.ctlVal);
        if (v.isPresent("operTm")) {
            this.operTm.value(v.operTm);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        origin(v.origin);
        ctlNum(v.ctlNum.value());
        t(v.t);
        test(v.test.value());
        addCause(v.addCause.value());
        return this;
    }
}
