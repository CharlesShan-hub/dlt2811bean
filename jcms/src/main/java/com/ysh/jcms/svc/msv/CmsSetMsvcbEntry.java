package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsMsvcbOptFlds;
import com.ysh.jcms.data.block.CmsSmpMod;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     svEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *     msvID       [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *     smpMod      [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate     [6] IMPLICIT INT16U OPTIONAL,
 *     optFlds     [7] IMPLICIT MSVCBOptFlds OPTIONAL
 * }  —  8.10.3
 *
 * Used by SetMSVCBValues request.
 */
public class CmsSetMsvcbEntry extends CmsType {

    public CmsObjectReference reference;
    public CmsBoolean         svEnaPresent;
    public CmsBoolean         svEna;              /* OPTIONAL */
    public CmsBoolean         msvIdPresent;
    public CmsUint8Array      msvId;              /* VisibleString129 OPTIONAL */
    public CmsBoolean         datSetPresent;
    public CmsObjectReference datSet;             /* OPTIONAL */
    public CmsBoolean         smpModPresent;
    public CmsSmpMod          smpMod;             /* OPTIONAL */
    public CmsBoolean         smpRatePresent;
    public CmsInt16U          smpRate;            /* OPTIONAL */
    public CmsBoolean         optFldsPresent;
    public CmsMsvcbOptFlds    optFlds;            /* OPTIONAL */

    public CmsSetMsvcbEntry() {
        this.reference      = new CmsObjectReference();
        this.svEnaPresent   = new CmsBoolean();
        this.svEna          = new CmsBoolean();
        this.msvIdPresent   = new CmsBoolean();
        this.msvId          = new CmsUint8Array();
        this.datSetPresent  = new CmsBoolean();
        this.datSet         = new CmsObjectReference();
        this.smpModPresent  = new CmsBoolean();
        this.smpMod         = new CmsSmpMod();
        this.smpRatePresent = new CmsBoolean();
        this.smpRate        = new CmsInt16U();
        this.optFldsPresent = new CmsBoolean();
        this.optFlds        = new CmsMsvcbOptFlds();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference,
            svEnaPresent, svEna,
            msvIdPresent, msvId,
            datSetPresent, datSet,
            smpModPresent, smpMod,
            smpRatePresent, smpRate,
            optFldsPresent, optFlds);
    }
}
