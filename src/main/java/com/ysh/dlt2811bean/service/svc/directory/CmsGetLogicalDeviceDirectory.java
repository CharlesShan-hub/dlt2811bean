package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CMS Service Code 0x51 — GetLogicalDeviceDirectory (read logical device directory).
 *
 * Corresponds to Table 24 in GB/T 45906.3-2025: GetLogicalDeviceDirectory service parameters.
 *
 * Service code: 0x51 (81)
 * Service interface: GetLogicalDeviceDirectory
 * Category: Directory service
 *
 * The GetLogicalDeviceDirectory service is used to retrieve the logical nodes
 * of a specified logical device. The referenceAfter parameter is used for
 * requesting information after a specified reference.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get logical device directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with logical node references</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ ldName[0..1]              ObjectName (OPTIONAL)             │
 * │ referenceAfter[0..1]      ObjectReference (OPTIONAL)        │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ lnReference[0..n]         SEQUENCE OF SubReference          │
 * │ moreFollows[0..1]         BOOLEAN (OPTIONAL)                │
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
 * GetLogicalDeviceDirectory-RequestPDU::= SEQUENCE {
 *   ldName            [0] IMPLICIT ObjectName OPTIONAL,
 *   referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetLogicalDeviceDirectory-ResponsePDU::= SEQUENCE {
 *   lnReference       [0] IMPLICIT SEQUENCE OF SubReference,
 *   moreFollows       [1] IMPLICIT BOOLEAN OPTIONAL
 * }
 *
 * GetLogicalDeviceDirectory-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetLogicalDeviceDirectory extends CmsAsdu<CmsGetLogicalDeviceDirectory> {

    // ==================== Fields based on Table 24 ====================

    // --- Request parameters ---
    // ldName [0..1] ObjectName (optional)
    public CmsObjectName ldName = new CmsObjectName();

    // referenceAfter [0..1] ObjectReference (optional)
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    // --- Response+ parameters ---
    // lnReference [0..n] SEQUENCE OF SubReference
    public CmsArray<CmsSubReference> lnReference = new CmsArray<>(CmsSubReference::new).capacity(100);

    // moreFollows [0..1] BOOLEAN (optional)
    public CmsBoolean moreFollows = new CmsBoolean();

    // --- Response- parameters ---
    // serviceError ServiceError
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    public CmsGetLogicalDeviceDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("ldName");
            registerField("referenceAfter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("lnReference");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetLogicalDeviceDirectory does not support " + messageType);
        }
    }

    public CmsGetLogicalDeviceDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }


    // ==================== Convenience Setters ====================

    public CmsGetLogicalDeviceDirectory ldName(String name) {
        this.ldName = new CmsObjectName(name);
        return this;
    }

    public CmsGetLogicalDeviceDirectory referenceAfter(String ref) {
        this.referenceAfter = new CmsObjectReference(ref);
        return this;
    }

    public CmsGetLogicalDeviceDirectory serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOGIC_DEVICE_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetLogicalDeviceDirectory copy() {
        CmsGetLogicalDeviceDirectory copy = new CmsGetLogicalDeviceDirectory(messageType());
        copy.reqId.set(reqId.get());
        copy.ldName = ldName.copy();
        copy.referenceAfter = referenceAfter.copy();
        copy.lnReference = lnReference.copy();
        copy.moreFollows = moreFollows.copy();
        copy.serviceError = serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetLogicalDeviceDirectory read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetLogicalDeviceDirectory) new CmsGetLogicalDeviceDirectory(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetLogicalDeviceDirectory service) {
        service.encode(pos);
    }
}
