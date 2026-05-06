package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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

    // ==================== Fields based on Table 33 ====================

    @CmsField(only = {"REQUEST"})
    public CmsObjectReference dataReference = new CmsObjectReference();
    
    @CmsField(only = {"REQUEST"})
    public CmsObjectReference referenceAfter = new CmsObjectReference();
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsGetDataDirectoryEntry> dataAttribute = new CmsArray<>(CmsGetDataDirectoryEntry::new).capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetDataDirectory() {
    }

    public CmsGetDataDirectory(MessageType messageType) {
        super(messageType);
    }

    public CmsGetDataDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetDataDirectory dataReference(String ref) {
        this.dataReference.set(ref);
        return this;
    }

    public CmsGetDataDirectory referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetDataDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    public CmsGetDataDirectory moreFollows(boolean moreFollows) {
        this.moreFollows.set(moreFollows);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_DIRECTORY;
    }
}
