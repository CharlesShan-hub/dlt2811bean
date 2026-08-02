package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectWithValueErrorPDU;
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

public class CmsSelectWithValueError extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField(optional = true) public CmsUtcTime operTm;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;
    @CmsField public CmsCheck check;
    @CmsField public CmsAddCause addCause;

    public CmsSelectWithValueError() { super(new InnerSelectWithValueErrorPDU()); }

    public CmsSelectWithValueError reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsSelectWithValueError reference(String v) { this.reference.value(v); return this; }
    public CmsSelectWithValueError ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsSelectWithValueError operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsSelectWithValueError origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsSelectWithValueError ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsSelectWithValueError t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsSelectWithValueError test(boolean v) { this.test.value(v); return this; }
    public CmsSelectWithValueError check(CmsCheck v) { this.check.value(v); return this; }
    public CmsSelectWithValueError addCause(int v) { this.addCause.value(v); return this; }

    public CmsSelectWithValueError value(CmsSelectWithValueError v) {
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
        check(v.check);
        addCause(v.addCause.value());
        return this;
    }
}
