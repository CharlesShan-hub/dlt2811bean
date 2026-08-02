package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerOperateErrorPDU;
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
 * Operate-ErrorPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, ctlVal
 * [1] IMPLICIT Data, origin [3] IMPLICIT Originator, ctlNum [4] IMPLICIT Int8U,
 * t [5] IMPLICIT TimeStamp, test [6] IMPLICIT Boolean, check [7] IMPLICIT
 * Check, addCause [8] IMPLICIT AddCause } — 8.11.3
 */
public class CmsOperateError extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;
    @CmsField public CmsCheck check;
    @CmsField public CmsAddCause addCause;

    public CmsOperateError() { super(new InnerOperateErrorPDU()); }

    public CmsOperateError reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsOperateError reference(String v) { this.reference.value(v); return this; }
    public CmsOperateError ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsOperateError origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsOperateError ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsOperateError t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsOperateError test(boolean v) { this.test.value(v); return this; }
    public CmsOperateError check(CmsCheck v) { this.check.value(v); return this; }
    public CmsOperateError addCause(int v) { this.addCause.value(v); return this; }

    public CmsOperateError value(CmsOperateError v) {
        reference(v.reference.value());
        ctlVal(v.ctlVal);
        origin(v.origin);
        ctlNum(v.ctlNum.value());
        t(v.t);
        test(v.test.value());
        check(v.check);
        addCause(v.addCause.value());
        return this;
    }
}
