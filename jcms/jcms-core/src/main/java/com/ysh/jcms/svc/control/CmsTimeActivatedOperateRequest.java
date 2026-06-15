package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * TimeActivatedOperate-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * }  —  8.11.6
 *
 * Note: operTm is mandatory.
 */
public class CmsTimeActivatedOperateRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;
    public CmsData             ctlVal;
    public CmsTimeStamp        operTm;         /* mandatory */
    public CmsOriginator       origin;
    public CmsInt8U            ctlNum;
    public CmsTimeStamp        t;
    public CmsBoolean          test;
    public CmsCheck            check;

    public CmsTimeActivatedOperateRequest() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsObjectReference();
        this.ctlVal    = new CmsData();
        this.operTm    = new CmsTimeStamp();
        this.origin    = new CmsOriginator();
        this.ctlNum    = new CmsInt8U();
        this.t         = new CmsTimeStamp();
        this.test      = new CmsBoolean();
        this.check     = new CmsCheck();
    }
    
    public CmsTimeActivatedOperateRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsTimeActivatedOperateRequest reference(byte[] v) { this.reference.value(v); return this; }
    public CmsTimeActivatedOperateRequest reference(String v) { this.reference.value(v); return this; }
    public CmsTimeActivatedOperateRequest ctlVal(CmsData v) { this.ctlVal = v; return this; }
    public CmsTimeActivatedOperateRequest operTm(CmsTimeStamp v) { this.operTm = v; return this; }
    public CmsTimeActivatedOperateRequest origin(CmsOriginator v) { this.origin = v; return this; }
    public CmsTimeActivatedOperateRequest ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsTimeActivatedOperateRequest t(CmsTimeStamp v) { this.t = v; return this; }
    public CmsTimeActivatedOperateRequest test(boolean v) { this.test.value(v); return this; }
    public CmsTimeActivatedOperateRequest check(CmsCheck v) { this.check = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal, operTm, origin, ctlNum, t, test, check);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeTimeActivatedOperateRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeTimeActivatedOperateRequest(nativePtr, data); read(); }
}