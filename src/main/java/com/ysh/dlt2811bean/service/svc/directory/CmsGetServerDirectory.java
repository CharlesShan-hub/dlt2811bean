package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

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
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
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
 *     file-system      (2)  <- not supported
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

    @CmsField(only = {"REQUEST"})
    public CmsObjectClass objectClass = new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE);

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsObjectReference> reference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetServerDirectory() {
        super(ServiceName.GET_SERVER_DIRECTORY);
    }

    public CmsGetServerDirectory(MessageType messageType) {
        super(ServiceName.GET_SERVER_DIRECTORY, messageType);
    }

    public CmsGetServerDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ==================== Convenience Setters ====================

    public CmsGetServerDirectory referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetServerDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    public CmsGetServerDirectory moreFollows(boolean moreFollows) {
        this.moreFollows.set(moreFollows);
        return this;
    }
}