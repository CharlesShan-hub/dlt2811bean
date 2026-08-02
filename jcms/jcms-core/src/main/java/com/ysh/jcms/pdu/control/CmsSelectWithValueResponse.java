package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectWithValueResponsePDU;
import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

public class CmsSelectWithValueResponse extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData ctlVal;
    @CmsField(optional = true) public CmsUtcTime operTm;
    @CmsField public CmsOriginator origin;
    @CmsField public CmsInt8U ctlNum;
    @CmsField public CmsUtcTime t;
    @CmsField public CmsBoolean test;
    @CmsField public CmsCheck check;

    public CmsSelectWithValueResponse() { super(new InnerSelectWithValueResponsePDU()); }

    public CmsSelectWithValueResponse reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsSelectWithValueResponse reference(String v) { this.reference.value(v); return this; }
    public CmsSelectWithValueResponse ctlVal(CmsData v) { this.ctlVal.value(v); return this; }
    public CmsSelectWithValueResponse operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsSelectWithValueResponse origin(CmsOriginator v) { this.origin.value(v); return this; }
    public CmsSelectWithValueResponse ctlNum(int v) { this.ctlNum.value(v); return this; }
    public CmsSelectWithValueResponse t(CmsUtcTime v) { this.t.value(v); return this; }
    public CmsSelectWithValueResponse test(boolean v) { this.test.value(v); return this; }
    public CmsSelectWithValueResponse check(CmsCheck v) { this.check.value(v); return this; }
}
