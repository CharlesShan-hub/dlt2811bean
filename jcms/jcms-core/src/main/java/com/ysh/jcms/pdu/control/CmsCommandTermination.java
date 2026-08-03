package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerCommandTerminationRequestPDU;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsAddCause;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * CommandTermination-RequestPDU ::= SEQUENCE { reference [0] IMPLICIT
 * ObjectReference, ctlVal [1] IMPLICIT Data, operTm [2] IMPLICIT TimeStamp
 * OPTIONAL, origin [3] IMPLICIT Originator, ctlNum [4] IMPLICIT Int8U, t [5]
 * IMPLICIT TimeStamp, test [6] IMPLICIT Boolean, check [7] IMPLICIT Check,
 * addCause [8] IMPLICIT AddCause OPTIONAL } — 8.11.5
 *
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsCommandTermination extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsData ctlVal;
    @CmsField(optional = true)
    public CmsUtcTime operTm;
    @CmsField
    public CmsOriginator origin;
    @CmsField
    public CmsInt8U ctlNum;
    @CmsField
    public CmsUtcTime t;
    @CmsField
    public CmsBoolean test;
    @CmsField
    public CmsCheck check;
    @CmsField(optional = true)
    public CmsAddCause addCause;

    public CmsCommandTermination() {
        super(new InnerCommandTerminationRequestPDU());
    }

    public CmsCommandTermination reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsCommandTermination reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCommandTermination ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsCommandTermination operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsCommandTermination origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsCommandTermination ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsCommandTermination t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsCommandTermination test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsCommandTermination check(CmsCheck v) {
        this.check.value(v);
        return this;
    }
    public CmsCommandTermination addCause(int v) {
        this.addCause.value(v);
        setPresent("addCause", true);
        return this;
    }
}
