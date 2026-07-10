package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.time.CmsUtcTime;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Operate-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ObjectReference, ctlVal [1] IMPLICIT Data, origin [3] IMPLICIT Originator,
 * ctlNum [4] IMPLICIT INT8U, t [5] IMPLICIT TimeStamp, test [6] IMPLICIT
 * BOOLEAN, check [7] IMPLICIT Check } — 8.11.3
 */
public class CmsOperateRequest extends CmsType {

    public CmsReqId reqId;
    public CmsObjectReference reference;
    public CmsData ctlVal;
    public CmsOriginator origin;
    public CmsInt8U ctlNum;
    public CmsUtcTime t;
    public CmsBoolean test;
    public CmsCheck check;

    public CmsOperateRequest() {
        super(Codec.OPERATE_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsObjectReference();
        this.ctlVal = new CmsData();
        this.origin = new CmsOriginator();
        this.ctlNum = new CmsInt8U();
        this.t = new CmsUtcTime();
        this.test = new CmsBoolean();
        this.check = new CmsCheck();
    }

    public CmsOperateRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsOperateRequest reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsOperateRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsOperateRequest ctlVal(CmsData v) {
        this.ctlVal = v;
        return this;
    }
    public CmsOperateRequest origin(CmsOriginator v) {
        this.origin = v;
        return this;
    }
    public CmsOperateRequest ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsOperateRequest t(CmsUtcTime v) {
        this.t = v;
        return this;
    }
    public CmsOperateRequest test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsOperateRequest check(CmsCheck v) {
        this.check = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal, origin, ctlNum, t, test, check);
    }
}
