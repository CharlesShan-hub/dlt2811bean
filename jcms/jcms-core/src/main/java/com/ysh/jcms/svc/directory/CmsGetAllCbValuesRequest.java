package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ReferenceChoice,
 *     acsiClass       [1] IMPLICIT ACSIClass,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.3.6
 */
public class CmsGetAllCbValuesRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsReferenceChoice  reference;
    public CmsAcsiClass        acsiClass;
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */

    public CmsGetAllCbValuesRequest() {
        this.reqId           = new CmsReqId();
        this.reference       = new CmsReferenceChoice();
        this.acsiClass       = new CmsAcsiClass();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsObjectReference();
    }
    
    public CmsGetAllCbValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetAllCbValuesRequest reference(CmsReferenceChoice v) { this.reference = v; return this; }
    public CmsGetAllCbValuesRequest acsiClass(int v) { this.acsiClass.value(v); return this; }
    public CmsGetAllCbValuesRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsGetAllCbValuesRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsGetAllCbValuesRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, acsiClass, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetAllCbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetAllCbValuesRequest(nativePtr, data); read(); }
}