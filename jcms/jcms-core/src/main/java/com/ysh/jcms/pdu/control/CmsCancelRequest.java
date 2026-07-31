package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.InnerCancelRequestPDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * Cancel-RequestPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, ctlVal
 * [1] IMPLICIT Data, operTm [2] IMPLICIT TimeStamp OPTIONAL, origin [3]
 * IMPLICIT Originator, ctlNum [4] IMPLICIT Int8U, t [5] IMPLICIT TimeStamp,
 * test [6] IMPLICIT Boolean } — 8.11.4
 */
public class CmsCancelRequest extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField(optional = true) public CmsUtcTime operTm;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;

    public CmsCancelRequest() { super(new InnerCancelRequestPDU()); }

    public CmsCancelRequest reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsCancelRequest reference(String v) { this.reference.value(v); return this; }
    public CmsCancelRequest ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsCancelRequest operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsCancelRequest origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsCancelRequest ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsCancelRequest t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsCancelRequest test(boolean v) { this.test.value(v); return this; }

    public CmsCancelRequest value(CmsCancelRequest v) {
        reference(v.reference.value());
        ctlVal(v.ctlVal);
        if (v.isPresent("operTm")) {
            this.operTm.value(v.operTm);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        origin(v.origin);
        ctlNum(v.ctlNum.value());
        t(v.t);
        test(v.test.value());
        return this;
    }
}
