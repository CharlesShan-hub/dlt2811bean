package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * CreateDataSet-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     memberData          [2] IMPLICIT SEQUENCE OF DataRefFcEntry
 * }  —  8.5.3
 */
public class CmsCreateDataSetRequest extends CmsType {

    public CmsReqId                       reqId;
    public CmsObjectReference             datasetReference;
    public CmsBoolean                     refAfterPresent;
    public CmsObjectReference             refAfter;       /* OPTIONAL */
    public CmsArray<CmsDataRefFcEntry>    memberData;     /* SEQUENCE OF DataRefFcEntry */

    public CmsCreateDataSetRequest() { super(Codec.CREATE_DATA_SET_REQUEST);
        this.reqId            = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsObjectReference();
        this.memberData       = new CmsArray<>(CmsDataRefFcEntry.class);
    }
    
    public CmsCreateDataSetRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsCreateDataSetRequest datasetReference(byte[] v) { this.datasetReference.value(v); return this; }
    public CmsCreateDataSetRequest datasetReference(String v) { this.datasetReference.value(v); return this; }
    public CmsCreateDataSetRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsCreateDataSetRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsCreateDataSetRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }
    public CmsCreateDataSetRequest memberData(CmsArray<CmsDataRefFcEntry> v) { this.memberData = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter, memberData);
    }
}