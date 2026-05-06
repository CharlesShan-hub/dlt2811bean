package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsErrorGocbChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * 8.9.4 — GetGoCBValues.
 *
 * Corresponds to Table 60 in GB/T 45906.3-2025: GetGoCBValues service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGoCBValues
 * Category: General station event service
 *
 * The GetGoCBValues service is used to retrieve the configuration and state information
 * of one or more GOOSE Control Blocks (GoCBs) from the server. The client can request
 * multiple GoCBs in a single request, and the server may respond with partial results
 * using the moreFollows flag when the number of requested GoCBs is large.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for GoCB values</li>
 *   <li>RESPONSE_POSITIVE - Server response containing GoCB information</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ reference                 SEQUENCE OF ObjectReference        │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocb                      SEQUENCE OF CHOICE {               │
 * │   error                     ServiceError                     │
 * │   value                     GoCB                             │
 * │ }                                                            │
 * │ moreFollows                BOOLEAN (default TRUE)            │
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
 * GetGoCBValues-RequestPDU::= SEQUENCE {
 *   reference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetGoCBValues-ResponsePDU::= SEQUENCE {
 *   gocb         [0] IMPLICIT SEQUENCE OF CHOICE {
 *                      error    [0] IMPLICIT ServiceError,
 *                      value    [1] IMPLICIT GoCB
 *                 },
 *   moreFollows  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetGoCBValues-ErrorPDU::= ServiceError
 *
 * GoCB::= SEQUENCE {
 *   goEna        [1] IMPLICIT BOOLEAN,
 *   goID         [2] IMPLICIT VisibleString129,
 *   datSet       [3] IMPLICIT ObjectReference,
 *   confRev      [4] IMPLICIT INT32U,
 *   ndsCom       [5] IMPLICIT BOOLEAN,
 *   dstAddress   [6] IMPLICIT PHYCOMADR OPTIONAL
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetGoCBValues extends CmsAsdu<CmsGetGoCBValues> {

    // ==================== Fields based on Table 60 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsObjectReference> gocbReference = new CmsArray<>(CmsObjectReference::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsErrorGocbChoice> errorGocb = new CmsArray<>(CmsErrorGocbChoice::new).capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetGoCBValues() {
        super(ServiceName.GET_GOCBVALUES);
    }
    
    public CmsGetGoCBValues(MessageType messageType) {
        super(ServiceName.GET_GOCBVALUES, messageType);
    }

    public CmsGetGoCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetGoCBValues addGocbReference(String ref) {
        this.gocbReference.add(new CmsObjectReference(ref));
        return this;
    }

    public CmsGetGoCBValues addErrorGocbChoice(CmsErrorGocbChoice choice) {
        this.errorGocb.add(choice);
        return this;
    }

    public CmsGetGoCBValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}
