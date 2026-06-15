package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ReferenceChoice,
 *     fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.3.5
 */
public class CmsGetAllDataDefinitionRequest extends CmsType {

    public CmsReqId               reqId;
    public CmsReferenceChoice     reference;
    public CmsBoolean             fcPresent;
    public CmsFunctionalConstraint fc;            /* OPTIONAL */
    public CmsBoolean             refAfterPresent;
    public CmsObjectReference     refAfter;       /* OPTIONAL */

    public CmsGetAllDataDefinitionRequest() {
        this.reqId           = new CmsReqId();
        this.reference       = new CmsReferenceChoice();
        this.fcPresent       = new CmsBoolean();
        this.fc              = new CmsFunctionalConstraint();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, fcPresent, fc, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetAllDataDefinitionRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetAllDataDefinitionRequest(nativePtr, data); read(); }
}
