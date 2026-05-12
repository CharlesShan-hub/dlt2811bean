package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * 8.9.3 — CmsGetGOOSEElementNumber.
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
public class CmsGetGooseElementNumber extends CmsAsdu<CmsGetGooseElementNumber> {

    // ==================== Fields based on Table 59 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference gocbReference = new CmsObjectReference();

    @CmsField(only = {REQUEST})
    public CmsArray<CmsCreateDataSetEntry> memberData = new CmsArray<>(CmsCreateDataSetEntry::new);

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsObjectReference gocbRefResp = new CmsObjectReference();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsInt32U confRev = new CmsInt32U();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsObjectReference datSet = new CmsObjectReference();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsInt16U> memberOffset = new CmsArray<>(CmsInt16U::new);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetGooseElementNumber() {
        super(ServiceName.GET_GOOSE_ELEMENT_NUMBER);
    }

    public CmsGetGooseElementNumber(MessageType messageType) {
        super(ServiceName.GET_GOOSE_ELEMENT_NUMBER, messageType);
    }

    public CmsGetGooseElementNumber(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetGooseElementNumber gocbReference(String ref) { 
        this.gocbReference.set(ref); 
        return this; 
    }

    public CmsGetGooseElementNumber addMemberData(String reference, String fc) {
        this.memberData.add(new CmsCreateDataSetEntry().reference(reference).fc(fc));
        return this;
    }

    public CmsGetGooseElementNumber gocbRefResp(String ref) { 
        this.gocbRefResp.set(ref); 
        return this; 
    }

    public CmsGetGooseElementNumber confRev(long rev) { 
        this.confRev.set(rev); 
        return this; 
    }

    public CmsGetGooseElementNumber datSet(String ds) { 
        this.datSet.set(ds); 
        return this; 
    }

    public CmsGetGooseElementNumber addMemberOffset(int offset) { 
        this.memberOffset.add(new CmsInt16U(offset)); 
        return this; 
    }

    public CmsGetGooseElementNumber serviceError(int errorCode) { 
        this.serviceError.set(errorCode); 
        return this; 
    }
}
