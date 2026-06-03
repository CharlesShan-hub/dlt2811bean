package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.code.CmsLcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Log Control Block (LCB).
 *
 * <p>Table 40 — LCB class definition (IEC 61850-7-2):
 * <pre>
 * LOG-CONTROL-BLOCK ::= SEQUENCE {
 *     lcbName          ObjectName,
 *     lcbRef           ObjectReference,
 *     logEna           BOOLEAN,
 *     datSet           ObjectReference,
 *     optFlds          PACKED LIST {
 *         reason-for-inclusion   [0] BOOLEAN
 *     },
 *     bufTm            INT32U,
 *     trgOps           TriggerConditions,
 *     intgPd           INT32U,
 *     logRef           ObjectReference
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsLCB extends AbstractCmsCompound<CmsLCB> {

    public CmsObjectName lcbName = new CmsObjectName();
    public CmsObjectReference lcbRef = new CmsObjectReference();
    public CmsBoolean logEna = new CmsBoolean();
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsLcbOptFlds optFlds = new CmsLcbOptFlds();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsTriggerConditions trgOps = new CmsTriggerConditions();
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsObjectReference logRef = new CmsObjectReference();

    public CmsLCB() {
        super("LCB");
        registerField("lcbName");
        registerField("lcbRef");
        registerField("logEna");
        registerField("datSet");
        registerField("optFlds");
        registerField("bufTm");
        registerField("trgOps");
        registerField("intgPd");
        registerField("logRef");
    }

    @Override
    public CmsLCB copy() {
        CmsLCB copy = new CmsLCB();
        copy.lcbName = lcbName.copy();
        copy.lcbRef = lcbRef.copy();
        copy.logEna = logEna.copy();
        copy.datSet = datSet.copy();
        copy.optFlds = optFlds.copy();
        copy.bufTm = bufTm.copy();
        copy.trgOps = trgOps.copy();
        copy.intgPd = intgPd.copy();
        copy.logRef = logRef.copy();
        return copy;
    }
}