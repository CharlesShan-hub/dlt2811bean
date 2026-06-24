package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoReference-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT INT32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberData      [3] IMPLICIT SEQUENCE OF GoRefFcEntry
 * }  —  8.9.2
 */
public class CmsGetGoReferenceResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsObjectReference             gocbReference;
    public CmsInt32U                      confRev;
    public CmsObjectReference             datSet;
    public CmsArray<CmsGoRefFcEntry>      memberData;   /* SEQUENCE OF GoRefFcEntry */

    public CmsGetGoReferenceResponse() { super(Codec.GET_GO_REFERENCE_RESPONSE);
        this.reqId         = new CmsReqId();
        this.gocbReference = new CmsObjectReference();
        this.confRev       = new CmsInt32U();
        this.datSet        = new CmsObjectReference();
        this.memberData    = new CmsArray<>();
    }
    
    public CmsGetGoReferenceResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGoReferenceResponse gocbReference(byte[] v) { this.gocbReference.value(v); return this; }
    public CmsGetGoReferenceResponse gocbReference(String v) { this.gocbReference.value(v); return this; }
    public CmsGetGoReferenceResponse confRev(long v) { this.confRev.value(v); return this; }
    public CmsGetGoReferenceResponse datSet(byte[] v) { this.datSet.value(v); return this; }
    public CmsGetGoReferenceResponse datSet(String v) { this.datSet.value(v); return this; }
    public CmsGetGoReferenceResponse memberData(CmsArray<CmsGoRefFcEntry> v) { this.memberData = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocbReference, confRev, datSet, memberData);
    }
}