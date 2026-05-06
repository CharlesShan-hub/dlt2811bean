package com.ysh.dlt2811bean.service.svc.rpc;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsReqDataChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.datatypes.type.CmsType;

/**
 * CMS Service Code 0x72 — RpcCall (remote procedure call service).
 *
 * Corresponds to Table 81 in GB/T 45906.3-2025: RpcCall service parameters.
 *
 * Service code: 0x72 (114)
 * Service interface: RpcCall
 * Category: Remote Procedure Call service
 *
 * The RpcCall service is used by a client to request the server to execute a specified
 * remote method. The client provides the method reference and either the request data
 * (for new calls) or a call ID (for continuations or cancellations). The server responds
 * with the result data and optionally a next call ID for streaming or asynchronous
 * interactions.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to invoke a remote method</li>
 *   <li>RESPONSE_POSITIVE - Server response containing result data and optional next call ID</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ method                       VisibleString                   │
 * │ reqData/callID               CHOICE {                        │
 * │   reqData                    Data                            │
 * │   callID                     OCTET STRING                    │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ rspData                      Data                            │
 * │ nextCallID                   OCTET STRING (OPTIONAL)         │
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
 * RpcCall-RequestPDU:: = SEQUENCE {
 *   method         [0] IMPLICIT VisibleString,
 *   reqDataCallID  [1] IMPLICIT CHOICE {
 *     reqData       [0] IMPLICIT Data,
 *     callID        [1] IMPLICIT OCTET STRING
 *   }
 * }
 *
 * RpcCall-ResponsePDU:: = SEQUENCE {
 *   rspData        [0] IMPLICIT Data,
 *   nextCallID     [1] IMPLICIT OCTET STRING OPTIONAL
 * }
 *
 * RpcCall-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsRpcCall extends CmsAsdu<CmsRpcCall> {

    // ==================== Fields based on Table 81 ====================

    @CmsField(only = {"REQUEST"})
    public CmsVisibleString method = new CmsVisibleString().max(255);

    @CmsField(only = {"REQUEST"})
    public CmsReqDataChoice reqDataCallID = new CmsReqDataChoice();

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsData rspData = new CmsData<>();

    @CmsField(optional = true, only = {"RESPONSE_POSITIVE"})
    public CmsOctetString nextCallID = new CmsOctetString().max(255);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsRpcCall() {
    }

    public CmsRpcCall(MessageType messageType) {
        super(messageType);
    }

    public CmsRpcCall(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsRpcCall method(String method) {
        this.method.set(method);
        return this;
    }

    public CmsRpcCall reqData(CmsType<?> value) {
        this.reqDataCallID.selectReqData().reqData(value);
        return this;
    }

    public CmsRpcCall callID(byte[] id) {
        this.reqDataCallID.selectCallID().callID(id);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsRpcCall rspData(CmsType<?> value) {
        this.rspData = new CmsData(value);
        return this;
    }

    public CmsRpcCall nextCallID(byte[] id) {
        this.nextCallID.set(id);
        return this;
    }

    public CmsRpcCall serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RPC_CALL;
    }
}
