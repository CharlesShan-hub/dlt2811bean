package com.ysh.jcms.core.pdu.msv;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSendMSVMessagePDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.enumerate.CmsSmpMod;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsInt8U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SendMSVMessage-PDU ::= SEQUENCE {
 *     msvID           [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     smpCnt          [2] IMPLICIT INT16U,
 *     confRev         [3] IMPLICIT INT32U,
 *     refTm           [4] IMPLICIT TimeStamp OPTIONAL,
 *     smpSynch        [5] IMPLICIT INT8U,
 *     smpRate         [6] IMPLICIT INT16U OPTIONAL,
 *     simulation      [7] IMPLICIT BOOLEAN,
 *     sample          [8] IMPLICIT SEQUENCE OF Data,
 *     smpMod          [9] IMPLICIT SmpMod OPTIONAL
 * } — 8.10.1
 * }
 * </pre>
 *
 * <p>
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsSendMsvMessage extends CmsSequence {

    @CmsField
    public CmsString msvID;
    @CmsField(optional = true)
    public CmsObjectReference datSet;
    @CmsField
    public CmsInt16U smpCnt;
    @CmsField
    public CmsInt32U confRev;
    @CmsField(optional = true)
    public CmsUtcTime refTm;
    @CmsField
    public CmsInt8U smpSynch;
    @CmsField(optional = true)
    public CmsInt16U smpRate;
    @CmsField
    public CmsBoolean simulation;
    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> sample; /* SEQUENCE OF Data */
    @CmsField(optional = true)
    public CmsSmpMod smpMod;

    public CmsSendMsvMessage() {
        super(new InnerSendMSVMessagePDU());
        this.sample = new ArrayList<>();
    }

    public CmsSendMsvMessage msvID(String v) {
        this.msvID.value(v);
        return this;
    }
    public CmsSendMsvMessage msvID(byte[] v) {
        return msvID(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSendMsvMessage datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSendMsvMessage smpCnt(int v) {
        this.smpCnt.value(v);
        return this;
    }
    public CmsSendMsvMessage confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsSendMsvMessage refTm(CmsUtcTime v) {
        if (v != null) {
            this.refTm.value(v);
            setPresent("refTm", true);
        } else {
            setPresent("refTm", false);
        }
        return this;
    }
    public CmsSendMsvMessage smpSynch(int v) {
        this.smpSynch.value(v);
        return this;
    }
    public CmsSendMsvMessage smpRate(int v) {
        this.smpRate.value(v);
        setPresent("smpRate", true);
        return this;
    }
    public CmsSendMsvMessage simulation(boolean v) {
        this.simulation.value(v);
        return this;
    }
    public CmsSendMsvMessage sample(List<CmsData> v) {
        this.sample = v;
        return this;
    }
    public CmsSendMsvMessage smpMod(int v) {
        this.smpMod.value(v);
        setPresent("smpMod", true);
        return this;
    }
}
