package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.core.data.scalar.*;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsOctetString;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;

/**
 * <pre>
 * {@code
 * BRCB ::= SEQUENCE {
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
 *     purgeBuf        [11] IMPLICIT BOOLEAN,
 *     entryID         [12] IMPLICIT EntryID,
 *     timeOfEntry     [13] IMPLICIT EntryTime,
 *     resvTms         [14] IMPLICIT INT16 OPTIONAL,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * } — 8.7.2
 * }
 * </pre>
 */
public class CmsBrcb extends CmsSequence {
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsString rptID;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean rptEna;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsObjectReference datSet;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U confRev;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsRcbOptFlds optFlds;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U bufTm;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsInt16U sqNum;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsTriggerConditions trgOps;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U intgPd;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean gi;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean purgeBuf;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsEntryId entryID;
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBinaryTime timeOfEntry;
    @CmsField(optional = true)
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt16 resvTms;
    @CmsField(optional = true)
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsOctetString owner;

    public CmsBrcb() {
        super(new InnerBRCB());
    }

    public CmsBrcb rptID(String v) {
        this.rptID.value(v);
        return this;
    }
    public CmsBrcb rptEna(boolean v) {
        this.rptEna.value(v);
        return this;
    }
    public CmsBrcb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsBrcb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsBrcb optFlds(CmsRcbOptFlds v) {
        this.optFlds.value(v);
        return this;
    }
    public CmsBrcb bufTm(long v) {
        this.bufTm.value(v);
        return this;
    }
    public CmsBrcb sqNum(int v) {
        this.sqNum.value(v);
        return this;
    }
    public CmsBrcb trgOps(CmsTriggerConditions v) {
        this.trgOps.value(v);
        return this;
    }
    public CmsBrcb intgPd(long v) {
        this.intgPd.value(v);
        return this;
    }
    public CmsBrcb gi(boolean v) {
        this.gi.value(v);
        return this;
    }
    public CmsBrcb purgeBuf(boolean v) {
        this.purgeBuf.value(v);
        return this;
    }
    public CmsBrcb entryID(byte[] v) {
        this.entryID.value(v);
        return this;
    }
    public CmsBrcb resvTms(int v) {
        this.resvTms.value(v);
        setPresent("resvTms", true);
        return this;
    }
    public CmsBrcb owner(byte[] v) {
        if (v != null) {
            this.owner.value(v);
            setPresent("owner", true);
        } else {
            setPresent("owner", false);
        }
        return this;
    }

    /** Copy all field values from another CmsBrcb (fluent). */
    public CmsBrcb value(CmsBrcb v) {
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
        purgeBuf(v.purgeBuf.value());
        entryID(v.entryID.value());
        if (v.isPresent("resvTms")) {
            this.resvTms.value(v.resvTms.value());
            setPresent("resvTms", true);
        } else {
            setPresent("resvTms", false);
        }
        if (v.isPresent("owner")) {
            this.owner.value(v.owner.value());
            setPresent("owner", true);
        } else {
            setPresent("owner", false);
        }
        return this;
    }
}
