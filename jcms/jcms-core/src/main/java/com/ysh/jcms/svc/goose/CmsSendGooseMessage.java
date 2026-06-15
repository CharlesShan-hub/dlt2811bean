package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SendGOOSEMessage-PDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     goID            [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     goRef           [2] IMPLICIT ObjectReference OPTIONAL,
 *     t               [3] IMPLICIT TimeStamp,
 *     stNum           [4] IMPLICIT INT32U,
 *     sqNum           [5] IMPLICIT INT32U,
 *     simulation      [6] IMPLICIT BOOLEAN,
 *     confRev         [7] IMPLICIT INT32U,
 *     ndsCom          [8] IMPLICIT BOOLEAN,
 *     data            [9] IMPLICIT SEQUENCE OF Data
 * }  —  8.9.1
 *
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsSendGooseMessage extends CmsType {

    public CmsReqId            reqId;
    public CmsUint8Array       goId;           /* VisibleString129 */
    public CmsBoolean          datSetPresent;
    public CmsObjectReference  datSet;         /* OPTIONAL */
    public CmsBoolean          goRefPresent;
    public CmsObjectReference  goRef;          /* OPTIONAL */
    public CmsTimeStamp        t;
    public CmsInt32U           stNum;
    public CmsInt32U           sqNum;
    public CmsBoolean          simulation;
    public CmsInt32U           confRev;
    public CmsBoolean          ndsCom;
    public CmsArray<CmsData>   data;           /* SEQUENCE OF Data */

    public CmsSendGooseMessage() {
        this.reqId         = new CmsReqId();
        this.goId          = new CmsUint8Array();
        this.datSetPresent = new CmsBoolean();
        this.datSet        = new CmsObjectReference();
        this.goRefPresent  = new CmsBoolean();
        this.goRef         = new CmsObjectReference();
        this.t             = new CmsTimeStamp();
        this.stNum         = new CmsInt32U();
        this.sqNum         = new CmsInt32U();
        this.simulation    = new CmsBoolean();
        this.confRev       = new CmsInt32U();
        this.ndsCom        = new CmsBoolean();
        this.data          = new CmsArray<>(CmsData.class);
    }
    
    // -- chain setters --
    public CmsSendGooseMessage reqId(int v) { this.reqId.value(v); return this; }
    public CmsSendGooseMessage goId(byte[] v) { this.goId.value(v); return this; }
    public CmsSendGooseMessage goId(String v) { this.goId.value(v); return this; }
    public CmsSendGooseMessage datSetPresent(boolean v) { this.datSetPresent.value(v); return this; }
    public CmsSendGooseMessage datSet(byte[] v) { this.datSetPresent.value(v != null && v.length > 0); if (v != null) this.datSet.value(v); return this; }
    public CmsSendGooseMessage datSet(String v) { this.datSetPresent.value(v != null); if (v != null) this.datSet.value(v); return this; }
    public CmsSendGooseMessage goRefPresent(boolean v) { this.goRefPresent.value(v); return this; }
    public CmsSendGooseMessage goRef(byte[] v) { this.goRefPresent.value(v != null && v.length > 0); if (v != null) this.goRef.value(v); return this; }
    public CmsSendGooseMessage goRef(String v) { this.goRefPresent.value(v != null); if (v != null) this.goRef.value(v); return this; }
    public CmsSendGooseMessage t(CmsTimeStamp v) { this.t = v; return this; }
    public CmsSendGooseMessage stNum(long v) { this.stNum.value(v); return this; }
    public CmsSendGooseMessage sqNum(long v) { this.sqNum.value(v); return this; }
    public CmsSendGooseMessage simulation(boolean v) { this.simulation.value(v); return this; }
    public CmsSendGooseMessage confRev(long v) { this.confRev.value(v); return this; }
    public CmsSendGooseMessage ndsCom(boolean v) { this.ndsCom.value(v); return this; }
    public CmsSendGooseMessage data(CmsArray<CmsData> v) { this.data = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, goId,
            datSetPresent, datSet,
            goRefPresent, goRef,
            t, stNum, sqNum, simulation, confRev, ndsCom, data);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSendGooseMessage(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSendGooseMessage(nativePtr, data); read(); }
}