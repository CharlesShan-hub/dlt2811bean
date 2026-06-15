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
