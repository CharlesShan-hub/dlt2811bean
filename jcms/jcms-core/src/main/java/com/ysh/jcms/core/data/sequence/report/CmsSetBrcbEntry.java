package com.ysh.jcms.core.data.sequence.report;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousSetBRCBValuesRequestPDUBrcb;
import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsEntryId;
import com.ysh.jcms.core.data.scalar.CmsInt16;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * SetBRCBEntry ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     rptID           [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna          [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet          [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds         [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm           [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps          [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd          [9] IMPLICIT INT32U OPTIONAL,
 *     gi              [10] IMPLICIT BOOLEAN OPTIONAL,
 *     purgeBuf        [11] IMPLICIT BOOLEAN OPTIONAL,
 *     entryID         [12] IMPLICIT EntryID OPTIONAL,
 *     resvTms         [13] IMPLICIT INT16 OPTIONAL
 * } — 8.7.3 (inline within SetBRCBValues-RequestPDU)
 * }
 * </pre>
 */
public class CmsSetBrcbEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField(optional = true)
    public CmsString rptID;
    @CmsField(optional = true)
    public CmsBoolean rptEna;
    @CmsField(optional = true)
    public CmsObjectReference datSet;
    @CmsField(optional = true)
    public CmsRcbOptFlds optFlds;
    @CmsField(optional = true)
    public CmsInt32U bufTm;
    @CmsField(optional = true)
    public CmsTriggerConditions trgOps;
    @CmsField(optional = true)
    public CmsInt32U intgPd;
    @CmsField(optional = true)
    public CmsBoolean gi;
    @CmsField(optional = true)
    public CmsBoolean purgeBuf;
    @CmsField(optional = true)
    public CmsEntryId entryID;
    @CmsField(optional = true)
    public CmsInt16 resvTms;

    public CmsSetBrcbEntry() {
        super(new InnerAnonymousSetBRCBValuesRequestPDUBrcb());
    }

    public CmsSetBrcbEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSetBrcbEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSetBrcbEntry rptID(String v) {
        if (v != null) {
            this.rptID.value(v);
            setPresent("rptID", true);
        } else {
            setPresent("rptID", false);
        }
        return this;
    }
    public CmsSetBrcbEntry rptID(byte[] v) {
        return rptID(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetBrcbEntry rptEna(boolean v) {
        this.rptEna.value(v);
        setPresent("rptEna", true);
        return this;
    }
    public CmsSetBrcbEntry datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSetBrcbEntry datSet(byte[] v) {
        return datSet(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetBrcbEntry optFlds(CmsRcbOptFlds v) {
        if (v != null) {
            this.optFlds.value(v);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
    public CmsSetBrcbEntry bufTm(long v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }
    public CmsSetBrcbEntry trgOps(CmsTriggerConditions v) {
        if (v != null) {
            this.trgOps.value(v);
            setPresent("trgOps", true);
        } else {
            setPresent("trgOps", false);
        }
        return this;
    }
    public CmsSetBrcbEntry intgPd(long v) {
        this.intgPd.value(v);
        setPresent("intgPd", true);
        return this;
    }
    public CmsSetBrcbEntry gi(boolean v) {
        this.gi.value(v);
        setPresent("gi", true);
        return this;
    }
    public CmsSetBrcbEntry purgeBuf(boolean v) {
        this.purgeBuf.value(v);
        setPresent("purgeBuf", true);
        return this;
    }
    public CmsSetBrcbEntry entryID(byte[] v) {
        if (v != null) {
            this.entryID.value(v);
            setPresent("entryID", true);
        } else {
            setPresent("entryID", false);
        }
        return this;
    }
    public CmsSetBrcbEntry resvTms(int v) {
        this.resvTms.value(v);
        setPresent("resvTms", true);
        return this;
    }

    public CmsSetBrcbEntry value(CmsSetBrcbEntry v) {
        reference(v.reference.value());
        if (v.isPresent("rptID")) {
            this.rptID.value(v.rptID.value());
            setPresent("rptID", true);
        } else {
            setPresent("rptID", false);
        }
        if (v.isPresent("rptEna")) {
            this.rptEna.value(v.rptEna.value());
            setPresent("rptEna", true);
        } else {
            setPresent("rptEna", false);
        }
        if (v.isPresent("datSet")) {
            this.datSet.value(v.datSet.value());
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        if (v.isPresent("optFlds")) {
            this.optFlds.value(v.optFlds);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        if (v.isPresent("bufTm")) {
            this.bufTm.value(v.bufTm.value());
            setPresent("bufTm", true);
        } else {
            setPresent("bufTm", false);
        }
        if (v.isPresent("trgOps")) {
            this.trgOps.value(v.trgOps);
            setPresent("trgOps", true);
        } else {
            setPresent("trgOps", false);
        }
        if (v.isPresent("intgPd")) {
            this.intgPd.value(v.intgPd.value());
            setPresent("intgPd", true);
        } else {
            setPresent("intgPd", false);
        }
        if (v.isPresent("gi")) {
            this.gi.value(v.gi.value());
            setPresent("gi", true);
        } else {
            setPresent("gi", false);
        }
        if (v.isPresent("purgeBuf")) {
            this.purgeBuf.value(v.purgeBuf.value());
            setPresent("purgeBuf", true);
        } else {
            setPresent("purgeBuf", false);
        }
        if (v.isPresent("entryID")) {
            this.entryID.value(v.entryID.value());
            setPresent("entryID", true);
        } else {
            setPresent("entryID", false);
        }
        if (v.isPresent("resvTms")) {
            this.resvTms.value(v.resvTms.value());
            setPresent("resvTms", true);
        } else {
            setPresent("resvTms", false);
        }
        return this;
    }
}
