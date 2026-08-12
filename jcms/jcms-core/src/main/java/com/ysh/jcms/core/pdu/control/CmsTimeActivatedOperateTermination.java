package com.ysh.jcms.core.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerTimeActivatedOperateTerminationRequestPDU;
import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.enumerate.CmsAddCause;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt8U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * TimeActivatedOperateTermination-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause OPTIONAL
 * } — 8.11.7
 * }
 * </pre>
 *
 * <p>
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsTimeActivatedOperateTermination extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsData ctlVal;
    @CmsField
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

    public CmsTimeActivatedOperateTermination() {
        super(new InnerTimeActivatedOperateTerminationRequestPDU());
    }

    public CmsTimeActivatedOperateTermination reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsTimeActivatedOperateTermination reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination operTm(CmsUtcTime v) {
        this.operTm.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination check(CmsCheck v) {
        this.check.value(v);
        return this;
    }
    public CmsTimeActivatedOperateTermination addCause(int v) {
        this.addCause.value(v);
        setPresent("addCause", true);
        return this;
    }
}
