package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * GoCB ::= SEQUENCE { 7 fields } — 8.9.4
 *
 * OPTIONAL field (dstAddress) uses a CmsBoolean "present" flag before the
 * value.
 */
public class CmsGoCb extends CmsType {

    public CmsBoolean goEna;
    public CmsUint8Array goID;
    public CmsObjectReference datSet;
    public CmsInt32U confRev;
    public CmsBoolean ndsCom;
    public CmsBoolean dstAddress_present;
    public CmsPhyComAddr dstAddress; /* OPTIONAL */

    public CmsGoCb() {
        super(Codec.GO_CB);
        this.goEna = new CmsBoolean();
        this.goID = new CmsUint8Array();
        this.datSet = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.ndsCom = new CmsBoolean();
        this.dstAddress_present = new CmsBoolean();
        this.dstAddress = new CmsPhyComAddr();
    }

    public CmsGoCb goEna(boolean v) {
        this.goEna.value(v);
        return this;
    }
    public CmsGoCb goID(byte[] v) {
        this.goID.value(v);
        return this;
    }
    public CmsGoCb goID(String v) {
        this.goID.value(v);
        return this;
    }
    public CmsGoCb datSet(byte[] v) {
        this.datSet.value(v);
        return this;
    }
    public CmsGoCb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsGoCb confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsGoCb ndsCom(boolean v) {
        this.ndsCom.value(v);
        return this;
    }
    public CmsGoCb dstAddress_present(boolean v) {
        this.dstAddress_present.value(v);
        return this;
    }
    public CmsGoCb dstAddress(CmsPhyComAddr v) {
        this.dstAddress = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(goEna, goID, datSet, confRev, ndsCom, dstAddress_present, dstAddress);
    }
}
