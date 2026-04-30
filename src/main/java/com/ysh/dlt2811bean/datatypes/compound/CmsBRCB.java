package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Buffered Report Control Block (BRCB).
 *
 * <p>ASN.1 definition (IEC 61850-7-2 §14.2.2):
 * <pre>
 * BUFFERED-REPORT-CONTROL-BLOCK ::= SEQUENCE {
 *     brcbName         ObjectName,
 *     brcbRef          ObjectReference,
 *     rptID            VISIBLE STRING (SIZE (0..129)),
 *     rptEna           BOOLEAN,
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
 *     sqNum             INT16U,
 *     trgOps           TriggerConditions,
 *     intgPd           INT32U,
 *     gi               BOOLEAN,
 *     purgeBuf          BOOLEAN,
 *     entryID          EntryID,
 *     timeOfEntry      EntryTime,
 *     resvTms          INT16 OPTIONAL,
 *     owner            OCTET STRING (SIZE (64))
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsBRCB extends AbstractCmsCompound<CmsBRCB> {

    public CmsObjectName brcbName = new CmsObjectName();
    public CmsObjectReference brcbRef = new CmsObjectReference();
    public CmsVisibleString rptID = new CmsVisibleString().max(129);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsRcbOptFlds optFlds = new CmsRcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsInt16U sqNum = new CmsInt16U();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean purgeBuf = new CmsBoolean();
    public CmsEntryID entryID = new CmsEntryID();
    public CmsEntryTime timeOfEntry = new CmsEntryTime();
    public CmsInt16 resvTms = new CmsInt16();
    public CmsOctetString owner = new CmsOctetString().size(64);

    public CmsBRCB() {
        super("BRCB");
        registerField("brcbName");
        registerField("brcbRef");
        registerField("rptID");
        registerField("rptEna");
        registerField("datSet");
        registerField("confRev");
        registerField("optFlds");
        registerField("bufTm");
        registerField("sqNum");
        registerField("trgOps");
        registerField("intgPd");
        registerField("gi");
        registerField("purgeBuf");
        registerField("entryID");
        registerField("timeOfEntry");
        registerOptionalField("resvTms");
        registerField("owner");
    }

    @Override
    public CmsBRCB copy() {
        CmsBRCB copy = new CmsBRCB();
        copy.brcbName = brcbName.copy();
        copy.brcbRef = brcbRef.copy();
        copy.rptID = rptID.copy();
        copy.rptEna = rptEna.copy();
        copy.datSet = datSet.copy();
        copy.confRev = confRev.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        copy.sqNum = sqNum.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.gi = gi.copy();
        copy.purgeBuf = purgeBuf.copy();
        copy.entryID = entryID.copy();
        copy.timeOfEntry = timeOfEntry.copy();
        copy.resvTms = resvTms.copy();
        copy.owner = owner.copy();
        return copy;
    }
}