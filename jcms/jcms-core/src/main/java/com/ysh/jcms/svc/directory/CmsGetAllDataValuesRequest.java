package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllDataValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0]
 * IMPLICIT ReferenceChoice, fc [1] IMPLICIT FunctionalConstraint OPTIONAL,
 * referenceAfter [2] IMPLICIT ObjectReference OPTIONAL } — 8.3.4
 */
public class CmsGetAllDataValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsReferenceChoice reference;
    public CmsBoolean fcPresent;
    public CmsFC fc; /* OPTIONAL */
    public CmsBoolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetAllDataValuesRequest() {
        super(Codec.GET_ALL_DATA_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsReferenceChoice();
        this.fcPresent = new CmsBoolean();
        this.fc = new CmsFC();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetAllDataValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest reference(CmsReferenceChoice v) {
        this.reference = v;
        return this;
    }
    public CmsGetAllDataValuesRequest fcPresent(boolean v) {
        this.fcPresent.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest fc(int v) {
        this.fcPresent.value(true);
        this.fc.value(v);
        return this;
    }

    public CmsGetAllDataValuesRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetAllDataValuesRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, fcPresent, fc, refAfterPresent, refAfter);
    }
}
