package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.time.CmsUtcTime;
import com.ysh.jcms.data.control.CmsAddCause;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * TimeActivatedOperate-ErrorPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ObjectReference, ctlVal [1] IMPLICIT Data, operTm [2] IMPLICIT
 * TimeStamp, origin [3] IMPLICIT Originator, ctlNum [4] IMPLICIT INT8U, t [5]
 * IMPLICIT TimeStamp, test [6] IMPLICIT BOOLEAN, check [7] IMPLICIT Check,
 * addCause [8] IMPLICIT AddCause } — 8.11.6
 */
public class CmsTimeActivatedOperateError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference reference;
    public CmsData ctlVal;
    public CmsUtcTime operTm; /* mandatory */
    public CmsOriginator origin;
    public CmsInt8U ctlNum;
    public CmsUtcTime t;
    public CmsBoolean test;
    public CmsCheck check;
    public CmsAddCause addCause;

    public CmsTimeActivatedOperateError() {
        super(Codec.TIME_ACTIVATED_OPERATE_ERROR);
        this.reqId = new CmsReqId();
        this.reference = new CmsObjectReference();
        this.ctlVal = new CmsData();
        this.operTm = new CmsUtcTime();
        this.origin = new CmsOriginator();
        this.ctlNum = new CmsInt8U();
        this.t = new CmsUtcTime();
        this.test = new CmsBoolean();
        this.check = new CmsCheck();
        this.addCause = new CmsAddCause();
    }

    public CmsTimeActivatedOperateError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError ctlVal(CmsData v) {
        this.ctlVal = v;
        return this;
    }
    public CmsTimeActivatedOperateError operTm(CmsUtcTime v) {
        this.operTm = v;
        return this;
    }
    public CmsTimeActivatedOperateError origin(CmsOriginator v) {
        this.origin = v;
        return this;
    }
    public CmsTimeActivatedOperateError ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError t(CmsUtcTime v) {
        this.t = v;
        return this;
    }
    public CmsTimeActivatedOperateError test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsTimeActivatedOperateError check(CmsCheck v) {
        this.check = v;
        return this;
    }
    public CmsTimeActivatedOperateError addCause(int v) {
        this.addCause.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference, ctlVal, operTm, origin, ctlNum, t, test, check, addCause);
    }
}
