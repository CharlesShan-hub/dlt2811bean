package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * URCB ::= SEQUENCE { 13 fields }  —  8.7.4
 *
 * OPTIONAL field (owner) uses a CmsBoolean "present" flag before the value.
 */
public class CmsUrcb extends CmsType {

    public CmsUint8Array        rptID;      /* VisibleString */
    public CmsBoolean           rptEna;
    public CmsObjectReference   datSet;
    public CmsInt32U            confRev;
    public CmsRcbOptFlds        optFlds;
    public CmsInt32U            bufTm;
    public CmsInt16U            sqNum;
    public CmsTriggerConditions trgOps;
    public CmsInt32U            intgPd;
    public CmsBoolean           gi;
    public CmsBoolean           resv;
    public CmsBoolean           owner_present;
    public CmsUint8Array        owner;          /* OPTIONAL */

    public CmsUrcb() {
        this.rptID   = new CmsUint8Array();
        this.rptEna  = new CmsBoolean();
        this.datSet  = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.optFlds = new CmsRcbOptFlds();
        this.bufTm   = new CmsInt32U();
        this.sqNum   = new CmsInt16U();
        this.trgOps  = new CmsTriggerConditions();
        this.intgPd  = new CmsInt32U();
        this.gi      = new CmsBoolean();
        this.resv    = new CmsBoolean();
        this.owner_present = new CmsBoolean();
        this.owner   = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(rptID, rptEna, datSet, confRev, optFlds,
            bufTm, sqNum, trgOps, intgPd, gi, resv,
            owner_present, owner);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeUrcb(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeUrcb(nativePtr, data); read(); }
}
