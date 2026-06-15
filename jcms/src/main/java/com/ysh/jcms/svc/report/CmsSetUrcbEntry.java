package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsRcbOptFlds;
import com.ysh.jcms.data.block.CmsTriggerConditions;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     rptID       [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds     [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm       [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd      [9] IMPLICIT INT32U OPTIONAL,
 *     gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *     resv        [13] IMPLICIT BOOLEAN OPTIONAL
 * }  —  8.7.5
 */
public class CmsSetUrcbEntry extends CmsType {

    public CmsObjectReference    reference;
    public CmsBoolean            rptIdPresent;
    public CmsUint8Array         rptId;           /* VisibleString129 OPTIONAL */
    public CmsBoolean            rptEnaPresent;
    public CmsBoolean            rptEna;          /* OPTIONAL */
    public CmsBoolean            datSetPresent;
    public CmsObjectReference    datSet;          /* OPTIONAL */
    public CmsBoolean            optFldsPresent;
    public CmsRcbOptFlds         optFlds;         /* OPTIONAL */
    public CmsBoolean            bufTmPresent;
    public CmsInt32U             bufTm;           /* OPTIONAL */
    public CmsBoolean            trgOpsPresent;
    public CmsTriggerConditions  trgOps;          /* OPTIONAL */
    public CmsBoolean            intgPdPresent;
    public CmsInt32U             intgPd;          /* OPTIONAL */
    public CmsBoolean            giPresent;
    public CmsBoolean            gi;              /* OPTIONAL */
    public CmsBoolean            resvPresent;
    public CmsBoolean            resv;            /* OPTIONAL */

    public CmsSetUrcbEntry() {
        this.reference      = new CmsObjectReference();
        this.rptIdPresent   = new CmsBoolean();
        this.rptId          = new CmsUint8Array();
        this.rptEnaPresent  = new CmsBoolean();
        this.rptEna         = new CmsBoolean();
        this.datSetPresent  = new CmsBoolean();
        this.datSet         = new CmsObjectReference();
        this.optFldsPresent = new CmsBoolean();
        this.optFlds        = new CmsRcbOptFlds();
        this.bufTmPresent   = new CmsBoolean();
        this.bufTm          = new CmsInt32U();
        this.trgOpsPresent  = new CmsBoolean();
        this.trgOps         = new CmsTriggerConditions();
        this.intgPdPresent  = new CmsBoolean();
        this.intgPd         = new CmsInt32U();
        this.giPresent      = new CmsBoolean();
        this.gi             = new CmsBoolean();
        this.resvPresent    = new CmsBoolean();
        this.resv           = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference,
            rptIdPresent, rptId,
            rptEnaPresent, rptEna,
            datSetPresent, datSet,
            optFldsPresent, optFlds,
            bufTmPresent, bufTm,
            trgOpsPresent, trgOps,
            intgPdPresent, intgPd,
            giPresent, gi,
            resvPresent, resv);
    }
}
