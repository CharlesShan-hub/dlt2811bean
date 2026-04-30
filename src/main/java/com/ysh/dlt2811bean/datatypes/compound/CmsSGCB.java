package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Setting Group Control Block (SGCB).
 *
 * <p>Table 36 — SGCB class definition (IEC 61850-7-2):
 * <pre>
 * SETTING-GROUP-CONTROL-BLOCK ::= SEQUENCE {
 *     sgcbName         ObjectName,
 *     sgcbRef          ObjectReference,
 *     numOfSG          INT8U,
 *     actSG            INT8U,
 *     editSG           INT8U,
 *     cnfEdit          BOOLEAN,
 *     lActTm           TimeStamp,
 *     resvTms          INT16U OPTIONAL
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsSGCB extends AbstractCmsCompound<CmsSGCB> {

    public CmsObjectName sgcbName = new CmsObjectName();
    public CmsObjectReference sgcbRef = new CmsObjectReference();
    public CmsInt8U numOfSG = new CmsInt8U();
    public CmsInt8U actSG = new CmsInt8U();
    public CmsInt8U editSG = new CmsInt8U();
    public CmsBoolean cnfEdit = new CmsBoolean();
    public CmsUtcTime lActTm = new CmsUtcTime();
    public CmsInt16U resvTms = new CmsInt16U();

    public CmsSGCB() {
        super("SGCB");
        registerField("sgcbName");
        registerField("sgcbRef");
        registerField("numOfSG");
        registerField("actSG");
        registerField("editSG");
        registerField("cnfEdit");
        registerField("lActTm");
        registerOptionalField("resvTms");
    }

    @Override
    public CmsSGCB copy() {
        CmsSGCB copy = new CmsSGCB();
        copy.sgcbName = sgcbName.copy();
        copy.sgcbRef = sgcbRef.copy();
        copy.numOfSG = numOfSG.copy();
        copy.actSG = actSG.copy();
        copy.editSG = editSG.copy();
        copy.cnfEdit = cnfEdit.copy();
        copy.lActTm = lActTm.copy();
        copy.resvTms = resvTms.copy();
        return copy;
    }
}