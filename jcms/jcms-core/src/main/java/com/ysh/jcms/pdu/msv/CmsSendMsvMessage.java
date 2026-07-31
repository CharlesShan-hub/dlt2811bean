package com.ysh.jcms.pdu.msv;

import com.ysh.jcms.data.InnerSendMSVMessagePDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsSmpMod;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;

import java.util.ArrayList;
import java.util.List;

/**
 * SendMSVMessage-PDU ::= SEQUENCE {
 *     msvID       [0] IMPLICIT VisibleString (SIZE (0..129)),
 *     datSet      [1] IMPLICIT ObjectReference OPTIONAL,
 *     smpCnt      [2] IMPLICIT Int16U,
 *     confRev     [3] IMPLICIT Int32U,
 *     refTm       [4] IMPLICIT TimeStamp OPTIONAL,
 *     smpSynch    [5] IMPLICIT Int8U,
 *     smpRate     [6] IMPLICIT Int16U OPTIONAL,
 *     simulation  [7] IMPLICIT Boolean,
 *     sample      [8] IMPLICIT SEQUENCE OF Data,
 *     smpMod      [9] IMPLICIT SmpMod OPTIONAL
 * } — 8.10.1
 *
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsSendMsvMessage extends CmsSequence {

    @CmsField public CmsString msvID;
    @CmsField(optional = true) public CmsObjectReference datSet;
    @CmsField public CmsInt16U smpCnt;
    @CmsField public CmsInt32U confRev;
    @CmsField(optional = true) public CmsUtcTime refTm;
    @CmsField public CmsInt8U smpSynch;
    @CmsField(optional = true) public CmsInt16U smpRate;
    @CmsField public CmsBoolean simulation;
    @CmsField(sequenceOf = true, elementType = CmsData.class) public List<CmsData> sample; /* SEQUENCE OF Data */
    @CmsField(optional = true) public CmsSmpMod smpMod;

    public CmsSendMsvMessage() {
        super(new InnerSendMSVMessagePDU());
        this.sample = new ArrayList<>();
    }

    public CmsSendMsvMessage msvID(String v) { this.msvID.value(v); return this; }
    public CmsSendMsvMessage msvID(byte[] v) { return msvID(new String(v)); }
    public CmsSendMsvMessage datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSendMsvMessage smpCnt(int v) { this.smpCnt.value(v); return this; }
    public CmsSendMsvMessage confRev(long v) { this.confRev.value(v); return this; }
    public CmsSendMsvMessage refTm(CmsUtcTime v) {
        if (v != null) {
            this.refTm.value(v);
            setPresent("refTm", true);
        } else {
            setPresent("refTm", false);
        }
        return this;
    }
    public CmsSendMsvMessage smpSynch(int v) { this.smpSynch.value(v); return this; }
    public CmsSendMsvMessage smpRate(int v) {
        this.smpRate.value(v);
        setPresent("smpRate", true);
        return this;
    }
    public CmsSendMsvMessage simulation(boolean v) { this.simulation.value(v); return this; }
    public CmsSendMsvMessage sample(List<CmsData> v) { this.sample = v; return this; }
    public CmsSendMsvMessage smpMod(int v) {
        this.smpMod.value(v);
        setPresent("smpMod", true);
        return this;
    }
}
