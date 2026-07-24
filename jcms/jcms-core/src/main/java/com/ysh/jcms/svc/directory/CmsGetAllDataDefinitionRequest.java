package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataDefinition-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, fc [1] IMPLICIT FunctionalConstraint OPTIONAL,
 * referenceAfter [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.5
 */
public class CmsGetAllDataDefinitionRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsReferenceChoice reference;
    public CmsBoolean fcPresent;
    public CmsFC fc; /* OPTIONAL */
    public CmsBoolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetAllDataDefinitionRequest() {
        super(Codec.GET_ALL_DATA_DEFINITION_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsReferenceChoice();
        this.fcPresent = new CmsBoolean();
        this.fc = new CmsFC();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetAllDataDefinitionRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetAllDataDefinitionRequest fcPresent(boolean v) {
        this.fcPresent.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest fc(int v) {
        this.fcPresent.value(true);
        this.fc.value(v);
        return this;
    }

    public CmsGetAllDataDefinitionRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetAllDataDefinitionRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference, fcPresent, fc, refAfterPresent, refAfter);
    }
}
