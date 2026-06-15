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
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     lnReference     [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.3.2
 */
public class CmsGetLogicalDeviceDirectoryResponse extends CmsType {

    public CmsReqId                     reqId;
    public CmsArray<CmsSubReference>    lnReference;  /* SEQUENCE OF SubReference */
    public CmsBoolean                   moreFollows;  /* DEFAULT TRUE */

    public CmsGetLogicalDeviceDirectoryResponse() {
        this.reqId       = new CmsReqId();
        this.lnReference = new CmsArray<>(CmsSubReference.class);
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, lnReference, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalDeviceDirectoryResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalDeviceDirectoryResponse(nativePtr, data); read(); }
}
