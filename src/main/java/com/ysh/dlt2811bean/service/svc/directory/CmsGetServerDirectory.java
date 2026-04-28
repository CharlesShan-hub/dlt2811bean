package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 0x50 — GetServerDirectory (read server directory).
 *
 * Corresponds to Table 22 in GB/T 45906.3-2025: GetServerDirectory service parameters.
 *
 * Service code: 0x50 (80)
 * Service interface: GetServerDirectory
 * Category: Directory service
 *
 * The GetServerDirectory service is used to retrieve all logical device names.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get server directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with directory entries</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ objectClass                  ENUMERATED                      │
 * │ referenceAfter[0..1]         ObjectReference (OPTIONAL)      │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference[0..n]              SEQUENCE OF ObjectReference     │
 * │ moreFollows[0..1]            BOOLEAN (OPTIONAL)              │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASD:
 * ┌───U───────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetServerDirectory-RequestPDU:: = SEQUENCE {
 *   objectClass        [0] IMPLICIT INTEGER {
 *     reserved         (0),
 *     logical-device   (1),
 *     file-system      (2)
 *   } (0..2),
 *   referenceAfter     [1] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetServerDirectory-ResponsePDU:: = SEQUENCE {
 *   reference          [0] IMPLICIT SEQUENCE OF ObjectReference,
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetServerDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetServerDirectory extends CmsAsdu<CmsGetServerDirectory> {

    // ==================== Fields based on Table 22 ====================

    // --- Request parameters ---
    // objectClass ENUMERATED
    public CmsObjectClass objectClass = new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE);

    // referenceAfter [0..1] ObjectReference (optional)
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    // --- Response+ parameters ---
    // reference [0..n] SEQUENCE OF ObjectReference
    public CmsArray<CmsObjectReference> reference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    // moreFollows [0..1] BOOLEAN (optional)
    public CmsBoolean moreFollows = new CmsBoolean();

    // --- Response- parameters ---
    // serviceError ServiceError
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    public CmsGetServerDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("objectClass");
            registerField("referenceAfter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("reference");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetServerDirectory does not support " + messageType);
        }
    }

    public CmsGetServerDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ==================== Convenience Setters ====================

    public CmsGetServerDirectory referenceAfter(String ref) {
        this.referenceAfter = new CmsObjectReference(ref);
        return this;
    }

    public CmsGetServerDirectory serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceCode getServiceCode() {
        return ServiceCode.GET_SERVER_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetServerDirectory copy() {
        CmsGetServerDirectory copy = new CmsGetServerDirectory(messageType());
        copy.reqId.set(reqId.get());
        copy.objectClass = this.objectClass.copy();
        copy.referenceAfter = this.referenceAfter.copy();
        copy.reference = this.reference.copy();
        copy.moreFollows = this.moreFollows.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetServerDirectory read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetServerDirectory) new CmsGetServerDirectory(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetServerDirectory getServerDirectory) {
        getServerDirectory.encode(pos);
    }
}