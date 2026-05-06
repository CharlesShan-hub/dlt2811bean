package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * 8.9.2 读 GOOSE 引用服务 (GetGoReference)
 *
 * Corresponds to Table 58 in GB/T 45906.3-2025: GetGoReference service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGoReference
 * Category: General station event service
 *
 * The GetGoReference service is used to retrieve the references and functional
 * constraints of the members within a GOOSE Control Block (GoCB) dataset.
 * This service is typically used by a client to discover the structure of a
 * GOOSE dataset published by a server.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for GOOSE member references</li>
 *   <li>RESPONSE_POSITIVE - Server response containing dataset structure</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ memberOffset               INT16U (1..n)                     │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ confRev                    INT32U                            │
 * │ datSet                     ObjectReference                   │
 * │ memberData                 SEQUENCE OF SEQUENCE {            │
 * │   reference                ObjectReference                   │
 * │   fc                       FunctionalConstraint              │
 * │ }                                                            │
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
 * GetGoReference-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   memberOffset      [1] IMPLICIT SEQUENCE OF INT16U
 * }
 *
 * GetGoReference-ResponsePDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   confRev          [1] IMPLICIT INT32U,
 *   datSet           [2] IMPLICIT ObjectReference,
 *   memberData       [3] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetGoReference-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetGoReference extends CmsAsdu<CmsGetGoReference> {

    // ==================== Fields based on Table 58 ====================

    @CmsField(only = {"REQUEST"})
    public CmsObjectReference gocbReference = new CmsObjectReference();
    
    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsInt16U> memberOffset = new CmsArray<>(CmsInt16U::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsObjectReference gocbRefResp = new CmsObjectReference();
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsInt32U confRev = new CmsInt32U();
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsObjectReference datSet = new CmsObjectReference();
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsCreateDataSetEntry> memberData = new CmsArray<>(CmsCreateDataSetEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetGoReference() {
    }
    
    public CmsGetGoReference(MessageType messageType) {
        super(messageType);
    }

    public CmsGetGoReference(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetGoReference gocbReference(String ref) { 
        this.gocbReference.set(ref); 
        return this; 
    }

    public CmsGetGoReference addMemberOffset(int offset) { 
        this.memberOffset.add(new CmsInt16U(offset)); 
        return this; 
    }

    public CmsGetGoReference gocbRefResp(String ref) { 
        this.gocbRefResp.set(ref); 
        return this; 
    }

    public CmsGetGoReference confRev(long rev) { 
        this.confRev.set(rev); 
        return this; 
    }

    public CmsGetGoReference datSet(String ds) { 
        this.datSet.set(ds); 
        return this; 
    }

    public CmsGetGoReference addMemberData(String reference, String fc) {
        this.memberData.add(new CmsCreateDataSetEntry().reference(reference).fc(fc));
        return this;
    }
    
    public CmsGetGoReference serviceError(int errorCode) { 
        this.serviceError.set(errorCode); 
        return this; 
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.Get_Go_Reference;
    }
}
