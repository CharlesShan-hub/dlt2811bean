package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsLcbOptFlds;
import com.ysh.jcms.data.block.CmsTriggerConditions;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     logEna      [1] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet      [2] IMPLICIT ObjectReference OPTIONAL,
 *     trgOps      [3] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd      [4] IMPLICIT INT32U OPTIONAL,
 *     logRef      [5] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds     [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm       [7] IMPLICIT INT32U OPTIONAL
 * }  —  8.8.3
 *
 * Used by SetLCBValues request.
 */
public class CmsSetLcbEntry extends CmsType {

    public CmsObjectReference    reference;
    public CmsBoolean            logEnaPresent;
    public CmsBoolean            logEna;             /* OPTIONAL */
    public CmsBoolean            datSetPresent;
    public CmsObjectReference    datSet;             /* OPTIONAL */
    public CmsBoolean            trgOpsPresent;
    public CmsTriggerConditions  trgOps;             /* OPTIONAL */
    public CmsBoolean            intgPdPresent;
    public CmsInt32U             intgPd;             /* OPTIONAL */
    public CmsBoolean            logRefPresent;
    public CmsObjectReference    logRef;             /* OPTIONAL */
    public CmsBoolean            optFldsPresent;
    public CmsLcbOptFlds         optFlds;            /* OPTIONAL */
    public CmsBoolean            bufTmPresent;
    public CmsInt32U             bufTm;              /* OPTIONAL */

    public CmsSetLcbEntry() {
        this.reference      = new CmsObjectReference();
        this.logEnaPresent  = new CmsBoolean();
        this.logEna         = new CmsBoolean();
        this.datSetPresent  = new CmsBoolean();
        this.datSet         = new CmsObjectReference();
        this.trgOpsPresent  = new CmsBoolean();
        this.trgOps         = new CmsTriggerConditions();
        this.intgPdPresent  = new CmsBoolean();
        this.intgPd         = new CmsInt32U();
        this.logRefPresent  = new CmsBoolean();
        this.logRef         = new CmsObjectReference();
        this.optFldsPresent = new CmsBoolean();
        this.optFlds        = new CmsLcbOptFlds();
        this.bufTmPresent   = new CmsBoolean();
        this.bufTm          = new CmsInt32U();
    }
    
    public CmsSetLcbEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsSetLcbEntry reference(String v) { this.reference.value(v); return this; }
    public CmsSetLcbEntry logEnaPresent(boolean v) { this.logEnaPresent.value(v); return this; }
    public CmsSetLcbEntry logEna(boolean v) { this.logEna.value(v); return this; }
    public CmsSetLcbEntry datSetPresent(boolean v) { this.datSetPresent.value(v); return this; }
    public CmsSetLcbEntry datSet(byte[] v) { this.datSetPresent.value(v != null && v.length > 0); if (v != null) this.datSet.value(v); return this; }
    public CmsSetLcbEntry datSet(String v) { this.datSetPresent.value(v != null); if (v != null) this.datSet.value(v); return this; }
    public CmsSetLcbEntry trgOpsPresent(boolean v) { this.trgOpsPresent.value(v); return this; }
    public CmsSetLcbEntry trgOps(CmsTriggerConditions v) { this.trgOps = v; return this; }
    public CmsSetLcbEntry intgPdPresent(boolean v) { this.intgPdPresent.value(v); return this; }
    public CmsSetLcbEntry intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsSetLcbEntry logRefPresent(boolean v) { this.logRefPresent.value(v); return this; }
    public CmsSetLcbEntry logRef(byte[] v) { this.logRefPresent.value(v != null && v.length > 0); if (v != null) this.logRef.value(v); return this; }
    public CmsSetLcbEntry logRef(String v) { this.logRefPresent.value(v != null); if (v != null) this.logRef.value(v); return this; }
    public CmsSetLcbEntry optFldsPresent(boolean v) { this.optFldsPresent.value(v); return this; }
    public CmsSetLcbEntry optFlds(CmsLcbOptFlds v) { this.optFlds = v; return this; }
    public CmsSetLcbEntry bufTmPresent(boolean v) { this.bufTmPresent.value(v); return this; }
    public CmsSetLcbEntry bufTm(long v) { this.bufTm.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference,
            logEnaPresent, logEna,
            datSetPresent, datSet,
            trgOpsPresent, trgOps,
            intgPdPresent, intgPd,
            logRefPresent, logRef,
            optFldsPresent, optFlds,
            bufTmPresent, bufTm);
    }
}