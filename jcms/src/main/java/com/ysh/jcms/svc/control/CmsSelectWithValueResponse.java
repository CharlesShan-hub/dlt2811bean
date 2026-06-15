package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsTimeStamp;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectWithValue-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * }  —  8.11.2
 */
public class CmsSelectWithValueResponse extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;
    public CmsData             ctlVal;
    public CmsBoolean          operTmPresent;
    public CmsTimeStamp        operTm;         /* OPTIONAL */
    public CmsOriginator       origin;
    public CmsInt8U            ctlNum;
    public CmsTimeStamp        t;
    public CmsBoolean          test;
    public CmsCheck            check;

    public CmsSelectWithValueResponse() {
        this.reqId          = new CmsReqId();
        this.reference      = new CmsObjectReference();
        this.ctlVal         = new CmsData();
        this.operTmPresent  = new CmsBoolean();
        this.operTm         = new CmsTimeStamp();
        this.origin         = new CmsOriginator();
        this.ctlNum         = new CmsInt8U();
        this.t              = new CmsTimeStamp();
        this.test           = new CmsBoolean();
        this.check          = new CmsCheck();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, ctlVal,
            operTmPresent, operTm, origin, ctlNum, t, test, check);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectWithValueResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectWithValueResponse(nativePtr, data); read(); }
}
