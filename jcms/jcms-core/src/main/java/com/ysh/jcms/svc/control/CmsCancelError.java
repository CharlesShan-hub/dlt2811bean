package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.control.CmsAddCause;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Cancel-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     addCause        [8] IMPLICIT AddCause
 * }  —  8.11.4
 */
public class CmsCancelError extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;
    public CmsData             ctlVal;
    public CmsBoolean          operTmPresent;
    public CmsTimeStamp        operTm;         /* OPTIONAL */
    public CmsOriginator       origin;
    public CmsInt8U            ctlNum;
    public CmsTimeStamp        t;
    public CmsBoolean          test;
    public CmsAddCause         addCause;

    public CmsCancelError() {
        this.reqId          = new CmsReqId();
        this.reference      = new CmsObjectReference();
        this.ctlVal         = new CmsData();
        this.operTmPresent  = new CmsBoolean();
        this.operTm         = new CmsTimeStamp();
        this.origin         = new CmsOriginator();
        this.ctlNum         = new CmsInt8U();
        this.t              = new CmsTimeStamp();
        this.test           = new CmsBoolean();
        this.addCause       = new CmsAddCause();
    }
    
    // -- chain setters --
    public CmsCancelError reqId(int v) { this.reqId.value(v); return this; }
    public CmsCancelError reference(byte[] v) { this.reference.value(v); return this; }
    public CmsCancelError reference(String v) { this.reference.value(v); return this; }
    public CmsCancelError ctlVal(CmsData v) { this.ctlVal = v; return this; }
    public CmsCancelError operTmPresent(boolean v) { this.operTmPresent.value(v); return this; }
    public CmsCancelError operTm(CmsTimeStamp v) { this.operTm = v; return this; }
    public CmsCancelError origin(CmsOriginator v) { this.origin = v; return this; }
    public CmsCancelError ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsCancelError t(CmsTimeStamp v) { this.t = v; return this; }
    public CmsCancelError test(boolean v) { this.test.value(v); return this; }
    public CmsCancelError addCause(int v) { this.addCause.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal,
            operTmPresent, operTm, origin, ctlNum, t, test, addCause);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeCancelError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeCancelError(nativePtr, data); read(); }
}