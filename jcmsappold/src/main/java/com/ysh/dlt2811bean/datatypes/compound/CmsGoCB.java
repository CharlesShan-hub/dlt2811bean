package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * GOOSE Control Block (GoCB).
 *
 * <p>Table 42 — GOOSE control block class definition (IEC 61850-7-2):
 * <pre>
 * GOOSE-CONTROL-BLOCK ::= SEQUENCE {
 *     goCBName         ObjectName,
 *     goCBRef          ObjectReference,
 *     goEna            BOOLEAN,
 *     goID             VISIBLE STRING (SIZE (0..129)),
 *     datSet           ObjectReference,
 *     confRev          INT32U,
 *     ndsCom           BOOLEAN,
 *     dstAddress       PHYCOMADDR
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsGoCB extends AbstractCmsCompound<CmsGoCB> {

    public CmsObjectName goCBName = new CmsObjectName();
    public CmsObjectReference goCBRef = new CmsObjectReference();
    public CmsBoolean goEna = new CmsBoolean();
    public CmsVisibleString goID = new CmsVisibleString().max(129);
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsBoolean ndsCom = new CmsBoolean();
    public CmsPhyComAddr dstAddress = new CmsPhyComAddr();

    public CmsGoCB() {
        super("GoCB");
        registerField("goCBName");
        registerField("goCBRef");
        registerField("goEna");
        registerField("goID");
        registerField("datSet");
        registerField("confRev");
        registerField("ndsCom");
        registerField("dstAddress");
    }

    @Override
    public CmsGoCB copy() {
        CmsGoCB copy = new CmsGoCB();
        copy.goCBName = goCBName.copy();
        copy.goCBRef = goCBRef.copy();
        copy.goEna = goEna.copy();
        copy.goID = goID.copy();
        copy.datSet = datSet.copy();
        copy.confRev = confRev.copy();
        copy.ndsCom = ndsCom.copy();
        copy.dstAddress = dstAddress.copy();
        return copy;
    }
}