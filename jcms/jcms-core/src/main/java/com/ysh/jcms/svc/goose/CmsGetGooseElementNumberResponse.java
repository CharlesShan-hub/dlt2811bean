package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsGetGooseElementNumberResponse() { super(Codec.GET_GOOSE_ELEMENT_NUMBER_RESPONSE);
        this.reqId         = new CmsReqId();
        this.gocbReference = new CmsObjectReference();
        this.confRev       = new CmsInt32U();
        this.datSet        = new CmsObjectReference();
        this.memberOffset  = new CmsArray<>(CmsInt16U.class);
    }
    
    public CmsGetGooseElementNumberResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGooseElementNumberResponse gocbReference(byte[] v) { this.gocbReference.value(v); return this; }
    public CmsGetGooseElementNumberResponse gocbReference(String v) { this.gocbReference.value(v); return this; }
    public CmsGetGooseElementNumberResponse confRev(long v) { this.confRev.value(v); return this; }
    public CmsGetGooseElementNumberResponse datSet(byte[] v) { this.datSet.value(v); return this; }
    public CmsGetGooseElementNumberResponse datSet(String v) { this.datSet.value(v); return this; }
    public CmsGetGooseElementNumberResponse memberOffset(CmsArray<CmsInt16U> v) { this.memberOffset = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocbReference, confRev, datSet, memberOffset);
    }
}