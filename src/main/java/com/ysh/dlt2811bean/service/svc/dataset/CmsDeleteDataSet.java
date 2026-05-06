package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x37 — DeleteDataSet (delete data set).
 *
 * Corresponds to Table 38 in GB/T 45906.3-2025: DeleteDataSet service parameters.
 *
 * Service code: 0x37 (55)
 * Service interface: DeleteDataSet
 * Category: Data set service
 *
 * The DeleteDataSet service is used to delete a specified data set.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Delete data set request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no payload)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ datasetReference            ObjectReference                 │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ (No additional data)                                        │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * DeleteDataSet-RequestPDU::= SEQUENCE {
 *   datasetReference  [0] IMPLICIT ObjectReference
 * }
 *
 * DeleteDataSet-ResponsePDU::= SEQUENCE {
 *   -- NULL
 * }
 *
 * DeleteDataSet-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsDeleteDataSet extends CmsAsdu<CmsDeleteDataSet> {

    // ==================== Fields based on Table 38 ====================

    @CmsField(only = {"REQUEST"})
    public CmsObjectReference datasetReference = new CmsObjectReference();

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsDeleteDataSet() {
        super(ServiceName.DELETE_DATA_SET);
    }

    public CmsDeleteDataSet(MessageType messageType) {
        super(ServiceName.DELETE_DATA_SET, messageType);
    }

    public CmsDeleteDataSet(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsDeleteDataSet datasetReference(String ref) {
        this.datasetReference.set(ref);
        return this;
    }

    public CmsDeleteDataSet serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
