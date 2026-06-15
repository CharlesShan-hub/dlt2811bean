package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectActiveSG-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT ObjectReference,
 *     settingGroupNumber  [1] IMPLICIT INT8U
 * }  —  8.6.1
 */
public class CmsSelectActiveSgRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  sgcbReference;
    public CmsInt8U            settingGroupNumber;

    public CmsSelectActiveSgRequest() {
        this.reqId              = new CmsReqId();
        this.sgcbReference      = new CmsObjectReference();
        this.settingGroupNumber = new CmsInt8U();
    }
    
    // -- chain setters --
    public CmsSelectActiveSgRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsSelectActiveSgRequest sgcbReference(byte[] v) { this.sgcbReference.value(v); return this; }
    public CmsSelectActiveSgRequest sgcbReference(String v) { this.sgcbReference.value(v); return this; }
    public CmsSelectActiveSgRequest settingGroupNumber(int v) { this.settingGroupNumber.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sgcbReference, settingGroupNumber);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectActiveSgRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectActiveSgRequest(nativePtr, data); read(); }
}