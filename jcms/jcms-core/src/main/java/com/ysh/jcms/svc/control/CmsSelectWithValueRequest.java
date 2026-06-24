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
 * SelectWithValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * }  —  8.11.2
 */
public class CmsSelectWithValueRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;
    public CmsData             ctlVal;
    public CmsBoolean          operTmPresent;
    public CmsUtcTime        operTm;         /* OPTIONAL */
    public CmsOriginator       origin;
    public CmsInt8U            ctlNum;
    public CmsUtcTime        t;
    public CmsBoolean          test;
    public CmsCheck            check;

    public CmsSelectWithValueRequest() { super(Codec.SELECT_WITH_VALUE_REQUEST);
        this.reqId          = new CmsReqId();
        this.reference      = new CmsObjectReference();
        this.ctlVal         = new CmsData();
        this.operTmPresent  = new CmsBoolean();
        this.operTm         = new CmsUtcTime();
        this.origin         = new CmsOriginator();
        this.ctlNum         = new CmsInt8U();
        this.t              = new CmsUtcTime();
        this.test           = new CmsBoolean();
        this.check          = new CmsCheck();
    }
    
    public CmsSelectWithValueRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSelectWithValueRequest reference(byte[] v) { this.reference.value(v); return this; }
    public CmsSelectWithValueRequest reference(String v) { this.reference.value(v); return this; }
    public CmsSelectWithValueRequest ctlVal(CmsData v) { this.ctlVal = v; return this; }
    public CmsSelectWithValueRequest operTmPresent(boolean v) { this.operTmPresent.value(v); return this; }
    public CmsSelectWithValueRequest operTm(CmsUtcTime v) { this.operTm = v; return this; }
    public CmsSelectWithValueRequest origin(CmsOriginator v) { this.origin = v; return this; }
    public CmsSelectWithValueRequest ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsSelectWithValueRequest t(CmsUtcTime v) { this.t = v; return this; }
    public CmsSelectWithValueRequest test(boolean v) { this.test.value(v); return this; }
    public CmsSelectWithValueRequest check(CmsCheck v) { this.check = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal,
            operTmPresent, operTm, origin, ctlNum, t, test, check);
    }
}