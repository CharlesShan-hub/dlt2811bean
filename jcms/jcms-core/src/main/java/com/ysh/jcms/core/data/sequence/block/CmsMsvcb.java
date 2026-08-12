package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.bitarray.CmsMsvcbOptFlds;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.core.data.enumerate.CmsSmpMod;
import com.ysh.jcms.core.data.scalar.*;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.sequence.common.CmsPhyComAddr;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * MSVCB ::= SEQUENCE {
 *     svEna           [1] IMPLICIT BOOLEAN,
 *     msvID           [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     smpMod          [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate         [6] IMPLICIT INT16U,
 *     optFlds         [7] IMPLICIT MSVCBOptFlds,
 *     dstAddress      [8] IMPLICIT PHYCOMADDR OPTIONAL
 * } — 8.10.2
 * }
 * </pre>
 */
public class CmsMsvcb extends CmsSequence {
    @CmsField
    public CmsBoolean svEna;
    @CmsField
    public CmsString msvID;
    @CmsField
    public CmsObjectReference datSet;
    @CmsField
    public CmsInt32U confRev;
    @CmsField(optional = true)
    public CmsSmpMod smpMod;
    @CmsField
    public CmsInt16U smpRate;
    @CmsField
    public CmsMsvcbOptFlds optFlds;
    @CmsField(optional = true)
    public CmsPhyComAddr dstAddress;

    public CmsMsvcb() {
        super(new InnerMSVCB());
    }

    public CmsMsvcb svEna(boolean v) {
        this.svEna.value(v);
        return this;
    }
    public CmsMsvcb msvID(String v) {
        this.msvID.value(v);
        return this;
    }
    public CmsMsvcb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsMsvcb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsMsvcb smpMod(int v) {
        this.smpMod.value(v);
        setPresent("smpMod", true);
        return this;
    }
    public CmsMsvcb smpRate(int v) {
        this.smpRate.value(v);
        return this;
    }
    public CmsMsvcb optFlds(CmsMsvcbOptFlds v) {
        this.optFlds.value(v);
        return this;
    }
    public CmsMsvcb dstAddress(CmsPhyComAddr v) {
        if (v != null) {
            this.dstAddress.value(v);
            setPresent("dstAddress", true);
        } else {
            setPresent("dstAddress", false);
        }
        return this;
    }

    /** Copy all field values from another CmsMsvcb (fluent). */
    public CmsMsvcb value(CmsMsvcb v) {
        svEna(v.svEna.value());
        msvID(v.msvID.value());
        datSet(v.datSet.value());
        confRev(v.confRev.value());
        if (v.isPresent("smpMod")) {
            this.smpMod.value(v.smpMod.value());
            setPresent("smpMod", true);
        } else {
            setPresent("smpMod", false);
        }
        smpRate(v.smpRate.value());
        optFlds(v.optFlds);
        if (v.isPresent("dstAddress")) {
            this.dstAddress.value(v.dstAddress);
            setPresent("dstAddress", true);
        } else {
            setPresent("dstAddress", false);
        }
        return this;
    }
}
