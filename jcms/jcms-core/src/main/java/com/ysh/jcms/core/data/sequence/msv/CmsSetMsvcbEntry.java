package com.ysh.jcms.core.data.sequence.msv;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousSetMSVCBValuesRequestPDUMsvcb;
import com.ysh.jcms.core.data.bitarray.CmsMsvcbOptFlds;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.enumerate.CmsSmpMod;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * SetMSVCBValues-RequestPDU msvcb entry ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     svEna           [1] IMPLICIT BOOLEAN OPTIONAL,
 *     msvID           [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet          [3] IMPLICIT ObjectReference OPTIONAL,
 *     smpMod          [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate         [6] IMPLICIT INT16U OPTIONAL,
 *     optFlds         [7] IMPLICIT MSVCBOptFlds OPTIONAL
 * } — 8.10.3
 * }
 * </pre>
 */
public class CmsSetMsvcbEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField(optional = true)
    public CmsBoolean svEna;
    @CmsField(optional = true)
    public CmsString msvID;
    @CmsField(optional = true)
    public CmsObjectReference datSet;
    @CmsField(optional = true)
    public CmsSmpMod smpMod;
    @CmsField(optional = true)
    public CmsInt16U smpRate;
    @CmsField(optional = true)
    public CmsMsvcbOptFlds optFlds;

    public CmsSetMsvcbEntry() {
        super(new InnerAnonymousSetMSVCBValuesRequestPDUMsvcb());
    }

    public CmsSetMsvcbEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSetMsvcbEntry reference(byte[] v) {
        return reference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSetMsvcbEntry svEna(boolean v) {
        this.svEna.value(v);
        setPresent("svEna", true);
        return this;
    }
    public CmsSetMsvcbEntry msvID(String v) {
        if (v != null) {
            this.msvID.value(v);
            setPresent("msvID", true);
        } else {
            setPresent("msvID", false);
        }
        return this;
    }
    public CmsSetMsvcbEntry datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSetMsvcbEntry smpMod(int v) {
        this.smpMod.value(v);
        setPresent("smpMod", true);
        return this;
    }
    public CmsSetMsvcbEntry smpRate(int v) {
        this.smpRate.value(v);
        setPresent("smpRate", true);
        return this;
    }
    public CmsSetMsvcbEntry optFlds(CmsMsvcbOptFlds v) {
        if (v != null) {
            this.optFlds.value(v);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }

    /** Copy all field values from another entry (fluent). */
    public CmsSetMsvcbEntry value(CmsSetMsvcbEntry v) {
        reference(v.reference.value());
        if (v.isPresent("svEna")) {
            this.svEna.value(v.svEna.value());
            setPresent("svEna", true);
        } else {
            setPresent("svEna", false);
        }
        if (v.isPresent("msvID")) {
            this.msvID.value(v.msvID.value());
            setPresent("msvID", true);
        } else {
            setPresent("msvID", false);
        }
        if (v.isPresent("datSet")) {
            this.datSet.value(v.datSet.value());
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        if (v.isPresent("smpMod")) {
            this.smpMod.value(v.smpMod.value());
            setPresent("smpMod", true);
        } else {
            setPresent("smpMod", false);
        }
        if (v.isPresent("smpRate")) {
            this.smpRate.value(v.smpRate.value());
            setPresent("smpRate", true);
        } else {
            setPresent("smpRate", false);
        }
        if (v.isPresent("optFlds")) {
            this.optFlds.value(v.optFlds);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
}
