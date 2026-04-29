package com.ysh.dlt2811bean.service.svc.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x58 — GetDataDirectory (read data directory).
 *
 * Corresponds to Table 33 in GB/T 45906.3-2025: GetDataDirectory service parameters.
 *
 * Service code: 0x32 (50)
 * Service interface: GetDataDirectory
 * Category: Data access service
 *
 * The GetDataDirectory service is used to retrieve the references of all child data objects
 * and data attributes under a specified data object. The fc parameter indicates whether
 * functional constraints are included (attributes only).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with directory entries</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ dataReference               ObjectReference                │
 * │ referenceAfter              ObjectReference (OPTIONAL)      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ dataAttribute[0..n]         SEQUENCE OF SEQUENCE {          │
 * │   reference                 SubReference                    │
 * │   fc                        FunctionalConstraint (OPTIONAL) │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN DEFAULT TRUE            │
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
 * GetDataDirectory-RequestPDU::= SEQUENCE {
 *   dataReference     [0] IMPLICIT ObjectReference,
 *   referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetDataDirectory-ResponsePDU::= SEQUENCE {
 *   dataAttribute     [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT SubReference,
 *     fc              [1] IMPLICIT FunctionalConstraint OPTIONAL
 *   },
 *   moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataDirectory-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetDataDirectory extends CmsAsdu<CmsGetDataDirectory> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetDataDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetDataDirectory does not support " + messageType);
        }
    }

    public CmsGetDataDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetDataDirectory copy() {
        CmsGetDataDirectory copy = new CmsGetDataDirectory(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetDataDirectory read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetDataDirectory) new CmsGetDataDirectory(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetDataDirectory getDataDirectory) {
        getDataDirectory.encode(pos);
    }

}
