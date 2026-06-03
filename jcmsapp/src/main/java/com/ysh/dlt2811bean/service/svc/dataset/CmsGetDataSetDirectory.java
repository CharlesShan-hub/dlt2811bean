package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x39 — GetDataSetDirectory (read data set directory).
 *
 * Corresponds to Table 39 in GB/T 45906.3-2025: GetDataSetDirectory service parameters.
 *
 * Service code: 0x39 (57)
 * Service interface: GetDataSetDirectory
 * Category: Data set service
 *
 * The GetDataSetDirectory service is used to batch retrieve the references of data set members.
 * When referenceAfter is not specified in the request, the directory should be read starting from
 * the first member. When referenceAfter is specified, the directory should be read starting after
 * the specified member.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data set directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with member directory data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ datasetReference            ObjectReference                 │
 * │ referenceAfter              ObjectReference (OPTIONAL)      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ memberData[0..n]            SEQUENCE OF SEQUENCE {          │
 * │   reference                  ObjectReference                │
 * │   fc                         FunctionalConstraint           │
 * │ }                                                           │
 * │ moreFollows                  BOOLEAN DEFAULT TRUE           │
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
 * GetDataSetDirectory-RequestPDU::= SEQUENCE {
 *   datasetReference  [0] IMPLICIT ObjectReference,
 *   referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetDataSetDirectory-ResponsePDU::= SEQUENCE {
 *   memberData        [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   },
 *   moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataSetDirectory-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetDataSetDirectory extends CmsAsdu<CmsGetDataSetDirectory> {

    // ==================== Fields based on Table 39 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference datasetReference = new CmsObjectReference();

    @CmsField(optional = true, only = {REQUEST})
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsCreateDataSetEntry> memberData = new CmsArray<>(CmsCreateDataSetEntry::new);
    
    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetDataSetDirectory() {
        super(ServiceName.GET_DATA_SET_DIRECTORY);
    }

    public CmsGetDataSetDirectory(MessageType messageType) {
        super(ServiceName.GET_DATA_SET_DIRECTORY, messageType);
    }

    public CmsGetDataSetDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetDataSetDirectory datasetReference(String ref) {
        this.datasetReference.set(ref);
        return this;
    }

    public CmsGetDataSetDirectory referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetDataSetDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    public CmsGetDataSetDirectory addMemberData(String reference, String fc) {
        this.memberData.add(new CmsCreateDataSetEntry()
            .reference(reference)
            .fc(fc));
        return this;
    }
}
