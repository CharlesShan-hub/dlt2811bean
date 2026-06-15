package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.3.3
 */
public class CmsGetLogicalNodeDirectoryResponse extends CmsType {

    public CmsReqId                     reqId;
    public CmsArray<CmsSubReference>    reference;    /* SEQUENCE OF SubReference */
    public CmsBoolean                   moreFollows;  /* DEFAULT TRUE */

    public CmsGetLogicalNodeDirectoryResponse() {
        this.reqId       = new CmsReqId();
        this.reference   = new CmsArray<>(CmsSubReference.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalNodeDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalNodeDirectoryResponse(nativePtr, data); read(); }
}
