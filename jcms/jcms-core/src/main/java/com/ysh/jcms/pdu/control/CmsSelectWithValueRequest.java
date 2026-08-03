package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectWithValueRequestPDU;
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
 * <pre>
 * {@code
 * SelectWithValue-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * } — 8.11.2
 * }
 * </pre>
 */
public class CmsSelectWithValueRequest extends CmsSequence {

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
    @CmsField
    public CmsCheck check;

    public CmsSelectWithValueRequest() {
        super(new InnerSelectWithValueRequestPDU());
    }

    public CmsSelectWithValueRequest reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSelectWithValueRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSelectWithValueRequest ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsSelectWithValueRequest operTm(CmsUtcTime v) {
        if (v != null) {
            this.operTm.value(v);
            setPresent("operTm", true);
        } else {
            setPresent("operTm", false);
        }
        return this;
    }
    public CmsSelectWithValueRequest origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsSelectWithValueRequest ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsSelectWithValueRequest t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsSelectWithValueRequest test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsSelectWithValueRequest check(CmsCheck v) {
        this.check.value(v);
        return this;
    }
}
