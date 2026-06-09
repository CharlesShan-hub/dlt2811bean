package com.ysh.jcms2.data.block;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.data.common.*;
import com.ysh.jcms2.data.scalar.*;
import com.ysh.jcms2.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * GoCB ::= SEQUENCE { 7 fields }  —  8.9.4
 *
 * OPTIONAL field (dstAddress) uses a CmsBoolean "present" flag before the value.
 */
public class CmsGoCb extends CmsType {

    public CmsBoolean          goEna;
    public CmsUint8Array       goID;
    public CmsObjectReference  datSet;
    public CmsInt32U           confRev;
    public CmsBoolean          ndsCom;
    public CmsBoolean          dstAddress_present;
    public CmsPhyComAddr       dstAddress;     /* OPTIONAL */

    public CmsGoCb() {
        this.goEna    = new CmsBoolean();
        this.goID     = new CmsUint8Array();
        this.datSet   = new CmsObjectReference();
        this.confRev  = new CmsInt32U();
        this.ndsCom   = new CmsBoolean();
        this.dstAddress_present = new CmsBoolean();
        this.dstAddress = new CmsPhyComAddr();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(goEna, goID, datSet, confRev, ndsCom,
                             dstAddress_present, dstAddress);
    }
}
