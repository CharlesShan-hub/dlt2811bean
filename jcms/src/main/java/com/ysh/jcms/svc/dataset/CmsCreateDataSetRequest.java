package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsCreateDataSetRequest() {
        this.reqId            = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsObjectReference();
        this.memberData       = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter, memberData);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeCreateDataSetRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeCreateDataSetRequest(nativePtr, data); read(); }
}
