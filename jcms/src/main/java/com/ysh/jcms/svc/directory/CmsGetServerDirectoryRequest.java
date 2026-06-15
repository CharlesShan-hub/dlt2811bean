package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetServerDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     objectClass     [0] IMPLICIT ObjectClass,
 *     referenceAfter  [1] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.3.1
 */
public class CmsGetServerDirectoryRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectClass      objectClass;
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */

    public CmsGetServerDirectoryRequest() {
        this.reqId           = new CmsReqId();
        this.objectClass     = new CmsObjectClass();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, objectClass, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetServerDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetServerDirectoryRequest(nativePtr, data); read(); }
}
