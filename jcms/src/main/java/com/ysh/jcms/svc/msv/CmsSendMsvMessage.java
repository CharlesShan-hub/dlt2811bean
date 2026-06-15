package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.block.CmsSmpMod;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SendMSVMessage-PDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     msvID           [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     smpCnt          [2] IMPLICIT INT16U,
 *     confRev         [3] IMPLICIT INT32U,
 *     refTm           [4] IMPLICIT TimeStamp OPTIONAL,
 *     smpSynch        [5] IMPLICIT INT8U,
 *     smpRate         [6] IMPLICIT INT16U OPTIONAL,
 *     simulation      [7] IMPLICIT BOOLEAN,
 *     sample          [8] IMPLICIT SEQUENCE OF Data,
 *     smpMod          [9] IMPLICIT SmpMod OPTIONAL
 * }  —  8.10.1
 *
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsSendMsvMessage extends CmsType {

    public CmsReqId            reqId;
    public CmsUint8Array       msvId;          /* VisibleString129 */
    public CmsBoolean          datSetPresent;
    public CmsObjectReference  datSet;         /* OPTIONAL */
    public CmsInt16U           smpCnt;
    public CmsInt32U           confRev;
    public CmsBoolean          refTmPresent;
    public CmsTimeStamp        refTm;          /* OPTIONAL */
    public CmsInt8U            smpSynch;
    public CmsBoolean          smpRatePresent;
    public CmsInt16U           smpRate;        /* OPTIONAL */
    public CmsBoolean          simulation;
    public CmsArray<CmsData>   sample;         /* SEQUENCE OF Data */
    public CmsBoolean          smpModPresent;
    public CmsSmpMod           smpMod;         /* OPTIONAL */

    public CmsSendMsvMessage() {
        this.reqId          = new CmsReqId();
        this.msvId          = new CmsUint8Array();
        this.datSetPresent  = new CmsBoolean();
        this.datSet         = new CmsObjectReference();
        this.smpCnt         = new CmsInt16U();
        this.confRev        = new CmsInt32U();
        this.refTmPresent   = new CmsBoolean();
        this.refTm          = new CmsTimeStamp();
        this.smpSynch       = new CmsInt8U();
        this.smpRatePresent = new CmsBoolean();
        this.smpRate        = new CmsInt16U();
        this.simulation     = new CmsBoolean();
        this.sample         = new CmsArray<>(CmsData.class);
        this.smpModPresent  = new CmsBoolean();
        this.smpMod         = new CmsSmpMod();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, msvId,
            datSetPresent, datSet,
            smpCnt, confRev,
            refTmPresent, refTm,
            smpSynch,
            smpRatePresent, smpRate,
            simulation, sample,
            smpModPresent, smpMod);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSendMsvMessage(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSendMsvMessage(nativePtr, data); read(); }
}
