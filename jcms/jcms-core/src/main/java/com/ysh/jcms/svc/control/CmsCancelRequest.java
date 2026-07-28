package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.time.CmsUtcTime;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Cancel-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ObjectReference, ctlVal [1] IMPLICIT Data, operTm [2] IMPLICIT TimeStamp
 * OPTIONAL, origin [3] IMPLICIT Originator, ctlNum [4] IMPLICIT INT8U, t [5]
 * IMPLICIT TimeStamp, test [6] IMPLICIT BOOLEAN } — 8.11.4
 */
public class CmsCancelRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference reference;
    public CmsData ctlVal;
    public CmsBoolean operTmPresent;
    public CmsUtcTime operTm; /* OPTIONAL */
    public CmsOriginator origin;
    public CmsInt8U ctlNum;
    public CmsUtcTime t;
    public CmsBoolean test;

    public CmsCancelRequest() {
        super(Codec.CANCEL_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsObjectReference();
        this.ctlVal = new CmsData();
        this.operTmPresent = new CmsBoolean();
        this.operTm = new CmsUtcTime();
        this.origin = new CmsOriginator();
        this.ctlNum = new CmsInt8U();
        this.t = new CmsUtcTime();
        this.test = new CmsBoolean();
    }

    public CmsCancelRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsCancelRequest reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsCancelRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCancelRequest ctlVal(CmsData v) {
        this.ctlVal = v;
        return this;
    }
    public CmsCancelRequest operTmPresent(boolean v) {
        this.operTmPresent.value(v);
        return this;
    }
    public CmsCancelRequest operTm(CmsUtcTime v) {
        this.operTm = v;
        return this;
    }
    public CmsCancelRequest origin(CmsOriginator v) {
        this.origin = v;
        return this;
    }
    public CmsCancelRequest ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsCancelRequest t(CmsUtcTime v) {
        this.t = v;
        return this;
    }
    public CmsCancelRequest test(boolean v) {
        this.test.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference, ctlVal, operTmPresent, operTm, origin, ctlNum, t, test);
    }
}
