package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
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

    public CmsMsvcb() {
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

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(svEna, msvID, datSet, confRev,
            smpMod_present, smpMod, smpRate, optFlds,
            dstAddress_present, dstAddress);
    }
}
