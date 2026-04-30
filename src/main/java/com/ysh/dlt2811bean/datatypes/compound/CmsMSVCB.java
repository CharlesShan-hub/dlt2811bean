package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.code.CmsMsvcbOptFlds;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsSmpMod;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Multicast Sampled Value Control Block (MSVCB).
 *
 * <p>Table 44 — MSVCB class definition (IEC 61850-7-2):
 * <pre>
 * MULTICAST-SAMPLED-VALUE-CONTROL-BLOCK ::= SEQUENCE {
 *     msvCBName        ObjectName,
 *     msvCBRef         ObjectReference,
 *     svEna            BOOLEAN DEFAULT FALSE,
 *     msvID            VISIBLE STRING (SIZE (0..129)),
 *     datSet           ObjectReference,
 *     confRev          INT32U,
 *     smpMod           ENUMERATED {smpPerPeriod(0), smpPerSecond(1), secPerSmp(2)},
 *     smpRate          INT16U,
 *     optFlds          PACKED LIST {
 *         refresh-time          [0] BOOLEAN DEFAULT FALSE,
 *         reserved              [1] BOOLEAN DEFAULT FALSE,
 *         sample-rate           [2] BOOLEAN DEFAULT FALSE,
 *         data-set-name         [3] BOOLEAN DEFAULT FALSE
 *     },
 *     dstAddress       PHYCOMADDR
 * }
 * </pre>
 */
@Getter
@Accessors(fluent = true)
public class CmsMSVCB extends AbstractCmsCompound<CmsMSVCB> {

    public CmsObjectName msvCBName = new CmsObjectName();
    public CmsObjectReference msvCBRef = new CmsObjectReference();
    public CmsBoolean svEna = new CmsBoolean();
    public CmsVisibleString msvID = new CmsVisibleString().max(129);
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsSmpMod smpMod = new CmsSmpMod();
    public CmsInt16U smpRate = new CmsInt16U();
    public CmsMsvcbOptFlds optFlds = new CmsMsvcbOptFlds();
    public CmsPhyComAddr dstAddress = new CmsPhyComAddr();

    public CmsMSVCB() {
        super("MSVCB");
        registerField("msvCBName");
        registerField("msvCBRef");
        registerField("svEna");
        registerField("msvID");
        registerField("datSet");
        registerField("confRev");
        registerField("smpMod");
        registerField("smpRate");
        registerField("optFlds");
        registerField("dstAddress");
    }

    @Override
    public CmsMSVCB copy() {
        CmsMSVCB copy = new CmsMSVCB();
        copy.msvCBName = msvCBName.copy();
        copy.msvCBRef = msvCBRef.copy();
        copy.svEna = svEna.copy();
        copy.msvID = msvID.copy();
        copy.datSet = datSet.copy();
        copy.confRev = confRev.copy();
        copy.smpMod = smpMod.copy();
        copy.smpRate = smpRate.copy();
        copy.optFlds = optFlds.copy();
        copy.dstAddress = dstAddress.copy();
        return copy;
    }
}