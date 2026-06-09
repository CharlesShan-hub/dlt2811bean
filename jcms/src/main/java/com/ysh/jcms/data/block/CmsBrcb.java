package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * BRCB ::= SEQUENCE { 15 fields }  —  8.4
 *
 * OPTIONAL fields (resvTms, owner) use a CmsBoolean "present" flag
 * before the value.
 */
public class CmsBrcb extends CmsType {

    public CmsUint8Array        rptID;
    public CmsBoolean           rptEna;
    public CmsObjectReference   datSet;
    public CmsInt32U            confRev;
    public CmsRcbOptFlds        optFlds;
    public CmsInt32U            bufTm;
    public CmsInt16U            sqNum;
    public CmsTriggerConditions trgOps;
    public CmsInt32U            intgPd;
    public CmsBoolean           gi;
    public CmsBoolean           purgeBuf;
    public CmsEntryId           entryID;
    public CmsEntryTime         timeOfEntry;
    public CmsBoolean           resvTms_present;
    public CmsInt16             resvTms;        /* OPTIONAL */
    public CmsBoolean           owner_present;
    public CmsUint8Array        owner;          /* OPTIONAL */

    public CmsBrcb() {
        this.rptID         = new CmsUint8Array();
        this.rptEna        = new CmsBoolean();
        this.datSet        = new CmsObjectReference();
        this.confRev       = new CmsInt32U();
        this.optFlds       = new CmsRcbOptFlds();
        this.bufTm         = new CmsInt32U();
        this.sqNum         = new CmsInt16U();
        this.trgOps        = new CmsTriggerConditions();
        this.intgPd        = new CmsInt32U();
        this.gi            = new CmsBoolean();
        this.purgeBuf      = new CmsBoolean();
        this.entryID       = new CmsEntryId();
        this.timeOfEntry   = new CmsEntryTime();
        this.resvTms_present = new CmsBoolean();
        this.resvTms       = new CmsInt16();
        this.owner_present = new CmsBoolean();
        this.owner         = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(rptID, rptEna, datSet, confRev, optFlds,
            bufTm, sqNum, trgOps, intgPd, gi, purgeBuf, entryID, timeOfEntry,
            resvTms_present, resvTms, owner_present, owner);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeBrcb(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeBrcb(nativePtr, data); read(); }
}
