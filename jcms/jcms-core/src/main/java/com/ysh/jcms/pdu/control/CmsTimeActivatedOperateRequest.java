package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerTimeActivatedOperateRequestPDU;
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
 * TimeActivatedOperate-RequestPDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * } — 8.11.6
 * }
 * </pre>
 */
public class CmsTimeActivatedOperateRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsData ctlVal;
    @CmsField
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

    public CmsTimeActivatedOperateRequest() {
        super(new InnerTimeActivatedOperateRequestPDU());
    }

    public CmsTimeActivatedOperateRequest reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsTimeActivatedOperateRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest ctlVal(CmsData v) {
        this.ctlVal.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest operTm(CmsUtcTime v) {
        this.operTm.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest origin(CmsOriginator v) {
        this.origin.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest ctlNum(int v) {
        this.ctlNum.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsTimeActivatedOperateRequest check(CmsCheck v) {
        this.check.value(v);
        return this;
    }
}
