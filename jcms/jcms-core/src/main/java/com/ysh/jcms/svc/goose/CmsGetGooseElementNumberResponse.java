package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT INT32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberOffset    [3] IMPLICIT SEQUENCE OF INT16U
 * }  —  8.9.3
 */
public class CmsGetGooseElementNumberResponse extends CmsType {

    public CmsReqId              reqId;
    public CmsObjectReference    gocbReference;
    public CmsInt32U             confRev;
    public CmsObjectReference    datSet;
    public CmsArray<CmsInt16U>   memberOffset;  /* SEQUENCE OF INT16U */

    public CmsGetGooseElementNumberResponse() {
        this.reqId         = new CmsReqId();
        this.gocbReference = new CmsObjectReference();
        this.confRev       = new CmsInt32U();
        this.datSet        = new CmsObjectReference();
        this.memberOffset  = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocbReference, confRev, datSet, memberOffset);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGooseElementNumberResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGooseElementNumberResponse(nativePtr, data); read(); }
}
