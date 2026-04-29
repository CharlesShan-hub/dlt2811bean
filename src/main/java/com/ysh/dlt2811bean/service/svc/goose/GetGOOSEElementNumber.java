package com.ysh.dlt2811bean.service.svc.goose;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * 8.9.3 (GetGOOSEElementNumber)
 *
 * Corresponds to Table 59 in GB/T 45906.3-2025: GetGOOSEElementNumber service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGOOSEElementNumber
 * Category: General station event service
 *
 * The GetGOOSEElementNumber service is used to retrieve the sequence numbers
 * (offsets) of the data elements within a GOOSE dataset. This is complementary
 * to the GetGoReference service. While GetGoReference returns the names and
 * functional constraints, GetGOOSEElementNumber returns the positional indices
 * (memberOffset) of those elements within the dataset structure.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for element offsets</li>
 *   <li>RESPONSE_POSITIVE - Server response containing element offsets</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ memberData                 SEQUENCE OF SEQUENCE {            │
 * │   reference                ObjectReference                   │
 * │   fc                       FunctionalConstraint              │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ confRev                    INT32U                            │
 * │ datSet                     ObjectReference                   │
 * │ memberOffset               SEQUENCE OF INT16U                │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError               ServiceError                      │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetGOOSEElementNumber-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   memberData       [1] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetGOOSEElementNumber-ResponsePDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   confRev          [1] IMPLICIT INT32U,
 *   datSet           [2] IMPLICIT ObjectReference,
 *   memberOffset     [3] IMPLICIT SEQUENCE OF INT16U
 * }
 *
 * GetGOOSEElementNumber-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class GetGOOSEElementNumber extends CmsAsdu<GetGOOSEElementNumber> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public GetGOOSEElementNumber(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetGOOSEElementNumber does not support " + messageType);
        }
    }

    public GetGOOSEElementNumber(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.Get_GOOSE_ElementNumber;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public GetGOOSEElementNumber copy() {
        GetGOOSEElementNumber copy = new GetGOOSEElementNumber(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static GetGOOSEElementNumber read(PerInputStream pis, MessageType messageType) throws Exception {
        return (GetGOOSEElementNumber) new GetGOOSEElementNumber(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, GetGOOSEElementNumber getGOOSEElementNumber) {
        getGOOSEElementNumber.encode(pos);
    }

}
