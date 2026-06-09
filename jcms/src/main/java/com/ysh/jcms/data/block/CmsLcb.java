package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import java.util.Arrays;
import java.util.List;

/**
 * LCB ::= SEQUENCE { 8 fields with optional }  —  8.8.2
 *
 * OPTIONAL fields (optFlds, bufTm) use a CmsBoolean "present" flag
 * before the value.
 */
public class CmsLcb extends CmsType {

    public CmsBoolean           logEna;
    public CmsObjectReference   datSet;
    public CmsTriggerConditions trgOps;
    public CmsInt32U            intgPd;
    public CmsObjectReference   logRef;
    public CmsBoolean           optFlds_present;
    public CmsLcbOptFlds        optFlds;        /* OPTIONAL */
    public CmsBoolean           bufTm_present;
    public CmsInt32U            bufTm;          /* OPTIONAL */

    public CmsLcb() {
        this.logEna    = new CmsBoolean();
        this.datSet    = new CmsObjectReference();
        this.trgOps    = new CmsTriggerConditions();
        this.intgPd    = new CmsInt32U();
        this.logRef    = new CmsObjectReference();
        this.optFlds_present = new CmsBoolean();
        this.optFlds   = new CmsLcbOptFlds();
        this.bufTm_present = new CmsBoolean();
        this.bufTm     = new CmsInt32U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(logEna, datSet, trgOps, intgPd, logRef,
                             optFlds_present, optFlds, bufTm_present, bufTm);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeLcb(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeLcb(nativePtr, data); read(); }
}
