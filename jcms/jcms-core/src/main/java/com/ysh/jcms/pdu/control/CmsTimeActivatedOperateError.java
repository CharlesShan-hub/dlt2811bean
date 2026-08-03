package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerTimeActivatedOperateErrorPDU;
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
 * <pre>
 * {@code
 * TimeActivatedOperate-ErrorPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause
 * } — 8.11.6
 * }
 * </pre>
 */
public class CmsTimeActivatedOperateError extends CmsSequence {

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
    @CmsField
    public CmsAddCause addCause;

    public CmsTimeActivatedOperateError() {
        super(new InnerTimeActivatedOperateErrorPDU());
    }

    public CmsTimeActivatedOperateError reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsTimeActivatedOperateError reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError operTm(CmsUtcTime v) {
        this.operTm.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError check(CmsCheck v) {
        this.check.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError addCause(int v) {
        this.addCause.value(v);
        return this;
    }

    public CmsTimeActivatedOperateError value(CmsTimeActivatedOperateError v) {
        reference(v.reference.value());
        ctlVal(v.ctlVal);
        operTm(v.operTm);
        origin(v.origin);
        ctlNum(v.ctlNum.value());
        t(v.t);
        test(v.test.value());
        check(v.check);
        addCause(v.addCause.value());
        return this;
    }
}
