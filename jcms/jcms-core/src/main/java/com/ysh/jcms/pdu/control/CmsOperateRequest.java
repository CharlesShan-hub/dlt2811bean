package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.InnerOperateRequestPDU;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * Operate-RequestPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference,
 * ctlVal [1] IMPLICIT Data, origin [3] IMPLICIT Originator, ctlNum [4] IMPLICIT
 * Int8U, t [5] IMPLICIT TimeStamp, test [6] IMPLICIT Boolean, check [7]
 * IMPLICIT Check } — 8.11.3
 */
public class CmsOperateRequest extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;
    @CmsField public CmsCheck check;

    public CmsOperateRequest() { super(new InnerOperateRequestPDU()); }

    public CmsOperateRequest reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsOperateRequest reference(String v) { this.reference.value(v); return this; }
    public CmsOperateRequest ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsOperateRequest origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsOperateRequest ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsOperateRequest t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsOperateRequest test(boolean v) { this.test.value(v); return this; }
    public CmsOperateRequest check(CmsCheck v) { this.check.value(v); return this; }

    public CmsOperateRequest value(CmsOperateRequest v) {
        reference(v.reference.value());
        ctlVal(v.ctlVal);
        origin(v.origin);
        ctlNum(v.ctlNum.value());
        t(v.t);
        test(v.test.value());
        check(v.check);
        return this;
    }
}
