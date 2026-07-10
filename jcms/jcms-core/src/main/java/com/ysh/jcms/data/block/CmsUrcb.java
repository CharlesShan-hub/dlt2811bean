package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * URCB ::= SEQUENCE { 13 fields } — 8.7.4
 *
 * OPTIONAL field (owner) uses a CmsBoolean "present" flag before the value.
 */
public class CmsUrcb extends CmsType {

    public CmsUint8Array rptID; /* VisibleString */
    public CmsBoolean rptEna;
    public CmsObjectReference datSet;
    public CmsInt32U confRev;
    public CmsRcbOptFlds optFlds;
    public CmsInt32U bufTm;
    public CmsInt16U sqNum;
    public CmsTriggerConditions trgOps;
    public CmsInt32U intgPd;
    public CmsBoolean gi;
    public CmsBoolean resv;
    public CmsBoolean owner_present;
    public CmsUint8Array owner; /* OPTIONAL */

    public CmsUrcb() {
        super(Codec.URCB);
        this.rptID = new CmsUint8Array();
        this.rptEna = new CmsBoolean();
        this.datSet = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.optFlds = new CmsRcbOptFlds();
        this.bufTm = new CmsInt32U();
        this.sqNum = new CmsInt16U();
        this.trgOps = new CmsTriggerConditions();
        this.intgPd = new CmsInt32U();
        this.gi = new CmsBoolean();
        this.resv = new CmsBoolean();
        this.owner_present = new CmsBoolean();
        this.owner = new CmsUint8Array();
    }

    public CmsUrcb rptID(byte[] v) {
        this.rptID.value(v);
        return this;
    }
    public CmsUrcb rptID(String v) {
        this.rptID.value(v);
        return this;
    }
    public CmsUrcb rptEna(boolean v) {
        this.rptEna.value(v);
        return this;
    }
    public CmsUrcb datSet(byte[] v) {
        this.datSet.value(v);
        return this;
    }
    public CmsUrcb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsUrcb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsUrcb optFlds(CmsRcbOptFlds v) {
        this.optFlds = v;
        return this;
    }
    public CmsUrcb bufTm(long v) {
        this.bufTm.value(v);
        return this;
    }
    public CmsUrcb sqNum(int v) {
        this.sqNum.value(v);
        return this;
    }
    public CmsUrcb trgOps(CmsTriggerConditions v) {
        this.trgOps = v;
        return this;
    }
    public CmsUrcb intgPd(long v) {
        this.intgPd.value(v);
        return this;
    }
    public CmsUrcb gi(boolean v) {
        this.gi.value(v);
        return this;
    }
    public CmsUrcb resv(boolean v) {
        this.resv.value(v);
        return this;
    }
    public CmsUrcb owner_present(boolean v) {
        this.owner_present.value(v);
        return this;
    }
    public CmsUrcb owner(byte[] v) {
        this.owner_present.value(v != null && v.length > 0);
        if (v != null)
            this.owner.value(v);
        return this;
    }
    public CmsUrcb owner(String v) {
        this.owner_present.value(v != null);
        if (v != null)
            this.owner.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(rptID, rptEna, datSet, confRev, optFlds, bufTm, sqNum, trgOps, intgPd, gi, resv, owner_present, owner);
    }
}
