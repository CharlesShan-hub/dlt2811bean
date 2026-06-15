package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.control.CmsAddCause;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectWithValue-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause
 * }  —  8.11.2
 */
public class CmsSelectWithValueError extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;
    public CmsData             ctlVal;
    public CmsBoolean          operTmPresent;
    public CmsTimeStamp        operTm;         /* OPTIONAL */
    public CmsOriginator       origin;
    public CmsInt8U            ctlNum;
    public CmsTimeStamp        t;
    public CmsBoolean          test;
    public CmsCheck            check;
    public CmsAddCause         addCause;

    public CmsSelectWithValueError() {
        this.reqId          = new CmsReqId();
        this.reference      = new CmsObjectReference();
        this.ctlVal         = new CmsData();
        this.operTmPresent  = new CmsBoolean();
        this.operTm         = new CmsTimeStamp();
        this.origin         = new CmsOriginator();
        this.ctlNum         = new CmsInt8U();
        this.t              = new CmsTimeStamp();
        this.test           = new CmsBoolean();
        this.check          = new CmsCheck();
        this.addCause       = new CmsAddCause();
    }
    
    // -- chain setters --
    public CmsSelectWithValueError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSelectWithValueError reference(byte[] v) { this.reference.value(v); return this; }
    public CmsSelectWithValueError reference(String v) { this.reference.value(v); return this; }
    public CmsSelectWithValueError ctlVal(CmsData v) { this.ctlVal = v; return this; }
    public CmsSelectWithValueError operTmPresent(boolean v) { this.operTmPresent.value(v); return this; }
    public CmsSelectWithValueError operTm(CmsTimeStamp v) { this.operTm = v; return this; }
    public CmsSelectWithValueError origin(CmsOriginator v) { this.origin = v; return this; }
    public CmsSelectWithValueError ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsSelectWithValueError t(CmsTimeStamp v) { this.t = v; return this; }
    public CmsSelectWithValueError test(boolean v) { this.test.value(v); return this; }
    public CmsSelectWithValueError check(CmsCheck v) { this.check = v; return this; }
    public CmsSelectWithValueError addCause(int v) { this.addCause.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal,
            operTmPresent, operTm, origin, ctlNum, t, test, check, addCause);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectWithValueError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectWithValueError(nativePtr, data); read(); }
}