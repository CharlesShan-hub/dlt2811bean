package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.13.3
 */
public class CmsGetRpcMethodDirectoryResponse extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsUint8Array>        reference;    /* SEQUENCE OF VisibleString */
    public CmsBoolean                     moreFollows;  /* DEFAULT TRUE */

    public CmsGetRpcMethodDirectoryResponse() {
        this.reqId       = new CmsReqId();
        this.reference   = new CmsArray<>(CmsUint8Array.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcMethodDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcMethodDirectoryResponse(nativePtr, data); read(); }
}
