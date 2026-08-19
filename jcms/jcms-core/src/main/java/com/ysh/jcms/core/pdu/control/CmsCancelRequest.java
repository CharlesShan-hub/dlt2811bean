package com.ysh.jcms.core.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerCancelRequestPDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt8U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * Cancel-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN
 * } — 8.11.4
 * }
 * </pre>
 */
public class CmsCancelRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsData ctlVal;
    @CmsField(optional = true)
    public CmsUtcTime operTm;
    @CmsField
    public CmsOriginator origin;
    @CmsField
    public CmsInt8U ctlNum;
    @CmsField
    public CmsUtcTime t;
    @CmsField
    public CmsBoolean test;

    public CmsCancelRequest() {
        super(new InnerCancelRequestPDU());
    }

    public CmsCancelRequest reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsCancelRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCancelRequest ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsCancelRequest ctlVal(Boolean v) {
        if (v != null) {
            this.ctlVal.value(new CmsData().alt_boolean(v));
        }
        return this;
    }
    public CmsCancelRequest operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsCancelRequest origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsCancelRequest origin(Integer v) {
        if (v != null) {
            this.origin.value(new CmsOriginator().orCat(v));
        }
        return this;
    }
    public CmsCancelRequest ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsCancelRequest ctlNum(Integer v) {
        if (v != null) {
            this.ctlNum.value(v);
        }
        return this;
    }
    public CmsCancelRequest t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsCancelRequest test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsCancelRequest test(Boolean v) {
        if (v != null) {
            this.test.value(v);
        }
        return this;
    }
}
