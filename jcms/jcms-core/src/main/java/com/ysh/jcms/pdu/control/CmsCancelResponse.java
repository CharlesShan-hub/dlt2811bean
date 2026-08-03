package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerCancelResponsePDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * Cancel-ResponsePDU ::= SEQUENCE {
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
public class CmsCancelResponse extends CmsSequence {

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

    public CmsCancelResponse() {
        super(new InnerCancelResponsePDU());
    }

    public CmsCancelResponse reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsCancelResponse reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCancelResponse ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsCancelResponse operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsCancelResponse origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsCancelResponse ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsCancelResponse t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsCancelResponse test(boolean v) {
        this.test.value(v);
        return this;
    }
}
