package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x5B — GetBRCBValues (get buffered report control block values).
 *
 * Corresponds to Table 47 in GB/T 45906.3-2025: GetBRCBValues service parameters.
 *
 * Service code: 0x5B (91)
 * Service interface: GetBRCBValues
 * Category: Reporting service
 *
 * The GetBRCBValues service is used to retrieve all attributes of the buffered
 * report control block (BRCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get BRCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or BRCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ brcbReference[0..n]        SEQUENCE OF ObjectReference      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ error/brcb[0..n]           SEQUENCE OF CHOICE {             │
 * │   error                     ServiceError                    │
 * │   brcb                      BRCB                            │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN (DEFAULT TRUE)          │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError                ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetBRCBValues-RequestPDU::= SEQUENCE {
 *   brcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetBRCBValues-ResponsePDU::= SEQUENCE {
 *   errorBrcb          [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error            [0] IMPLICIT ServiceError,
 *     brcb             [1] IMPLICIT BRCB
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetBRCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetBRCBValues extends CmsAsdu<CmsGetBRCBValues> {

    // ==================== Fields based on Table 47 ====================

    // --- Request parameters ---
    public CmsArray<CmsObjectReference> brcbReference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    // --- Response+ parameters ---
    public CmsArray<CmsErrorBrcbChoice> errorBrcb = new CmsArray<>(CmsErrorBrcbChoice::new).capacity(100);
    public CmsBoolean moreFollows = new CmsBoolean(true);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetBRCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("brcbReference");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("errorBrcb");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetBRCBValues does not support " + messageType);
        }
    }

    public CmsGetBRCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetBRCBValues addBrcbReference(String ref) {
        this.brcbReference.add(new CmsObjectReference(ref));
        return this;
    }

    public CmsGetBRCBValues addErrorChoice(CmsErrorBrcbChoice choice) {
        this.errorBrcb.add(choice);
        return this;
    }

    public CmsGetBRCBValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_BRCBVALUES;
    }
}
