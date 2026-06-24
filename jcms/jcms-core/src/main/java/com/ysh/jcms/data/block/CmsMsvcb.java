package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * MSVCB ::= SEQUENCE { 10 fields }  —  8.10.2
 *
 * OPTIONAL fields (smpMod, dstAddress) use a CmsBoolean "present" flag
 * before the value.
 */
public class CmsMsvcb extends CmsType {

    public CmsBoolean          svEna;
    public CmsUint8Array       msvID;
    public CmsObjectReference  datSet;
    public CmsInt32U           confRev;
    public CmsBoolean          smpMod_present;
    public CmsSmpMod           smpMod;         /* OPTIONAL */
    public CmsInt16U           smpRate;
    public CmsMsvcbOptFlds     optFlds;
    public CmsBoolean          dstAddress_present;
    public CmsPhyComAddr       dstAddress;     /* OPTIONAL */

    public CmsMsvcb() { super(Codec.MSVCB);
        this.svEna    = new CmsBoolean();
        this.msvID    = new CmsUint8Array();
        this.datSet   = new CmsObjectReference();
        this.confRev  = new CmsInt32U();
        this.smpMod_present = new CmsBoolean();
        this.smpMod   = new CmsSmpMod();
        this.smpRate  = new CmsInt16U();
        this.optFlds  = new CmsMsvcbOptFlds();
        this.dstAddress_present = new CmsBoolean();
        this.dstAddress = new CmsPhyComAddr();
    }
    
    public CmsMsvcb svEna(boolean v) { this.svEna.value(v); return this; }
    public CmsMsvcb msvID(byte[] v) { this.msvID.value(v); return this; }
    public CmsMsvcb msvID(String v) { this.msvID.value(v); return this; }
    public CmsMsvcb datSet(byte[] v) { this.datSet.value(v); return this; }
    public CmsMsvcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsMsvcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsMsvcb smpMod_present(boolean v) { this.smpMod_present.value(v); return this; }
    public CmsMsvcb smpMod(int v) { this.smpMod.value(v); return this; }
    public CmsMsvcb smpRate(int v) { this.smpRate.value(v); return this; }
    public CmsMsvcb optFlds(CmsMsvcbOptFlds v) { this.optFlds = v; return this; }
    public CmsMsvcb dstAddress_present(boolean v) { this.dstAddress_present.value(v); return this; }
    public CmsMsvcb dstAddress(CmsPhyComAddr v) { this.dstAddress = v; return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(svEna, msvID, datSet, confRev,
            smpMod_present, smpMod, smpRate, optFlds,
            dstAddress_present, dstAddress);
    }
}