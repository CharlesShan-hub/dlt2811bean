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
    
    public CmsGetLogicalDeviceDirectoryRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest ldNamePresent(boolean v) { this.ldNamePresent.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest ldName(byte[] v) { this.ldNamePresent.value(v != null && v.length > 0); if (v != null) this.ldName.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest ldName(String v) { this.ldNamePresent.value(v != null); if (v != null) this.ldName.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, ldNamePresent, ldName, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalDeviceDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalDeviceDirectoryRequest(nativePtr, data); read(); }
}