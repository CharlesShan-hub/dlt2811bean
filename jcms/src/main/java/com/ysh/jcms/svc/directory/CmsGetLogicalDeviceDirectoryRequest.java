package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     ldName          [0] IMPLICIT ObjectName OPTIONAL,
 *     referenceAfter  [1] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.3.2
 */
public class CmsGetLogicalDeviceDirectoryRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsBoolean          ldNamePresent;
    public CmsObjectName       ldName;         /* OPTIONAL */
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */

    public CmsGetLogicalDeviceDirectoryRequest() {
        this.reqId           = new CmsReqId();
        this.ldNamePresent   = new CmsBoolean();
        this.ldName          = new CmsObjectName();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, ldNamePresent, ldName, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalDeviceDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalDeviceDirectoryRequest(nativePtr, data); read(); }
}
