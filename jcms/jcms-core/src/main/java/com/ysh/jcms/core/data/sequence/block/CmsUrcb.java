package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsOctetString;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * URCB ::= SEQUENCE {
 *     rptID           [1] IMPLICIT VisibleString129,
 *     rptEna          [2] IMPLICIT BOOLEAN,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     optFlds         [5] IMPLICIT RCBOptFlds,
 *     bufTm           [6] IMPLICIT INT32U,
 *     sqNum           [7] IMPLICIT INT16U,
 *     trgOps          [8] IMPLICIT TriggerConditions,
 *     intgPd          [9] IMPLICIT INT32U,
 *     gi              [10] IMPLICIT BOOLEAN,
 *     resv            [14] IMPLICIT BOOLEAN,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * } — 8.7.4
 * }
 * </pre>
 */
public class CmsUrcb extends CmsSequence {
    @CmsField
    public CmsString rptID;
    @CmsField
    public CmsBoolean rptEna;
    @CmsField
    public CmsObjectReference datSet;
    @CmsField
    public CmsInt32U confRev;
    @CmsField
    public CmsRcbOptFlds optFlds;
    @CmsField
    public CmsInt32U bufTm;
    @CmsField
    public CmsInt16U sqNum;
    @CmsField
    public CmsTriggerConditions trgOps;
    @CmsField
    public CmsInt32U intgPd;
    @CmsField
    public CmsBoolean gi;
    @CmsField
    public CmsBoolean resv;
    @CmsField(optional = true)
    public CmsOctetString owner;

    public CmsUrcb() {
        super(new InnerURCB());
    }

    public CmsUrcb rptID(String v) {
        this.rptID.value(v);
        return this;
    }
    public CmsUrcb rptEna(boolean v) {
        this.rptEna.value(v);
        return this;
    }
    public CmsUrcb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsUrcb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsUrcb optFlds(CmsRcbOptFlds v) {
        this.optFlds.value(v);
        return this;
    }
    public CmsUrcb bufTm(long v) {
        this.bufTm.value(v);
        return this;
    }
    public CmsUrcb sqNum(int v) {
        this.sqNum.value(v);
        return this;
    }
    public CmsUrcb trgOps(CmsTriggerConditions v) {
        this.trgOps.value(v);
        return this;
    }
    public CmsUrcb intgPd(long v) {
        this.intgPd.value(v);
        return this;
    }
    public CmsUrcb gi(boolean v) {
        this.gi.value(v);
        return this;
    }
    public CmsUrcb resv(boolean v) {
        this.resv.value(v);
        return this;
    }
    public CmsUrcb owner(byte[] v) {
        if (v != null) {
            this.owner.value(v);
            setPresent("owner", true);
        } else {
            setPresent("owner", false);
        }
        return this;
    }

    /** Copy all field values from another CmsUrcb (fluent). */
    public CmsUrcb value(CmsUrcb v) {
        rptID(v.rptID.value());
        rptEna(v.rptEna.value());
        datSet(v.datSet.value());
        confRev(v.confRev.value());
        optFlds(v.optFlds);
        bufTm(v.bufTm.value());
        sqNum(v.sqNum.value());
        trgOps(v.trgOps);
        intgPd(v.intgPd.value());
        gi(v.gi.value());
        resv(v.resv.value());
        if (v.isPresent("owner")) {
            this.owner.value(v.owner.value());
            setPresent("owner", true);
        } else {
            setPresent("owner", false);
        }
        return this;
    }
}
