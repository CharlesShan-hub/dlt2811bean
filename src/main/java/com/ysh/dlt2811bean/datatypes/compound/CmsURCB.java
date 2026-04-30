package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Unbuffered Report Control Block (URCB).
 *
 * <p>Table 39 — URCB class definition (IEC 61850-7-2):
 * <pre>
 * UNBUFFERED-REPORT-CONTROL-BLOCK ::= SEQUENCE {
 *     urcbName         ObjectName,
 *     urcbRef          ObjectReference,
 *     rptID            VISIBLE STRING (SIZE (0..129)),
 *     rptEna           BOOLEAN,
 *     resv             BOOLEAN,
 *     datSet           ObjectReference,
 *     confRev          INT32U,
 *     optFlds          PACKED LIST {
 *         sequence-number       [0] BOOLEAN DEFAULT FALSE,
 *         report-time-stamp     [1] BOOLEAN DEFAULT FALSE,
 *         reason-for-inclusion   [2] BOOLEAN DEFAULT FALSE,
 *         data-set-name         [3] BOOLEAN DEFAULT FALSE,
 *         data-reference         [4] BOOLEAN DEFAULT FALSE,
 *         buffer-overflow        [5] BOOLEAN DEFAULT FALSE,
 *         entryID               [6] BOOLEAN DEFAULT FALSE,
 *         conf-revision         [7] BOOLEAN DEFAULT FALSE
 *     },
 *     bufTm            INT32U,
 *     sqNum             INT8U,
 *     trgOps           TriggerConditions,
 *     intgPd           INT32U,
 *     gi               BOOLEAN,
 *     owner            OCTET STRING (SIZE (64))
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsURCB extends AbstractCmsCompound<CmsURCB> {

    public CmsObjectName urcbName = new CmsObjectName();
    public CmsObjectReference urcbRef = new CmsObjectReference();
    public CmsVisibleString rptID = new CmsVisibleString().max(129);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsBoolean resv = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsRcbOptFlds optFlds = new CmsRcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsInt8U sqNum = new CmsInt8U();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsOctetString owner = new CmsOctetString().size(64);

    public CmsURCB() {
        super("URCB");
        registerField("urcbName");
        registerField("urcbRef");
        registerField("rptID");
        registerField("rptEna");
        registerField("resv");
        registerField("datSet");
        registerField("confRev");
        registerField("optFlds");
        registerField("bufTm");
        registerField("sqNum");
        registerField("trgOps");
        registerField("intgPd");
        registerField("gi");
        registerField("owner");
    }

    @Override
    public CmsURCB copy() {
        CmsURCB copy = new CmsURCB();
        copy.urcbName = urcbName.copy();
        copy.urcbRef = urcbRef.copy();
        copy.rptID = rptID.copy();
        copy.rptEna = rptEna.copy();
        copy.resv = resv.copy();
        copy.datSet = datSet.copy();
        copy.confRev = confRev.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        copy.sqNum = sqNum.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.gi = gi.copy();
        copy.owner = owner.copy();
        return copy;
    }
}