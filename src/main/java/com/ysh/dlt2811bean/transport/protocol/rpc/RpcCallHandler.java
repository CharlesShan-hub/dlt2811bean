package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class RpcCallHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(RpcCallHandler.class);

    private final AtomicInteger callIdSeq = new AtomicInteger(1);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RPC_CALL;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling RpcCall: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsRpcCall) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsRpcCall asdu = (CmsRpcCall) request.getAsdu();
        String method = asdu.method.get();

        if (method == null || method.isEmpty()) {
            log.warn("[Server] RpcCall with empty method");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int selected = asdu.reqDataCallID.getSelectedIndex();
        boolean isContinuation = (selected == 1);

        if (isContinuation) {
            byte[] callId = asdu.reqDataCallID.callID.get();
            log.debug("[Server] RpcCall continuation: method={}, callId={}", method, bytesToHex(callId));

            RpcContinuationState state = (RpcContinuationState) serverSession.getAttribute(callIdKey(callId));
            if (state == null) {
                log.warn("[Server] RpcCall continuation not found: callId={}", bytesToHex(callId));
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }

            serverSession.removeAttribute(callIdKey(callId));
            return executeMethod(serverSession, asdu, method, state);
        }

        log.debug("[Server] RpcCall new call: method={}", method);
        return executeMethod(serverSession, asdu, method, null);
    }

    private CmsApdu executeMethod(CmsServerSession session, CmsRpcCall asdu,
                                   String method, RpcContinuationState state) {
        String methodLower = method.toLowerCase();

        if (methodLower.equals("ping")) {
            return respondWithPong(asdu);
        }

        if (methodLower.equals("echo")) {
            if (state != null) {
                return respondWithData(asdu, new CmsVisibleString(state.echoData));
            }
            return respondWithData(asdu, asdu.reqDataCallID.reqData);
        }

        if (methodLower.startsWith("iterate")) {
            return handleIterate(session, asdu, state);
        }

        log.warn("[Server] RpcCall unknown method: {}", method);
        return buildNegativeResponse(asdu, CmsServiceError.CLASS_NOT_SUPPORTED);
    }

    private CmsApdu respondWithPong(CmsRpcCall asdu) {
        CmsRpcCall response = new CmsRpcCall(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .rspData(new CmsInt32U(1));
        log.debug("[Server] RpcCall pong");
        return new CmsApdu(response);
    }

    private CmsApdu respondWithData(CmsRpcCall asdu, Object data) {
        CmsRpcCall response = new CmsRpcCall(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        if (data instanceof CmsData) {
            response.rspData = (CmsData<?>) data;
        } else if (data instanceof CmsInt32U) {
            response.rspData((CmsInt32U) data);
        } else if (data instanceof CmsVisibleString) {
            response.rspData((CmsVisibleString) data);
        }
        log.debug("[Server] RpcCall response sent");
        return new CmsApdu(response);
    }

    private CmsApdu handleIterate(CmsServerSession session, CmsRpcCall asdu, RpcContinuationState state) {
        int start = 0;
        if (state != null) {
            start = state.offset;
        }

        int pageSize = 3;
        int total = 10;

        if (start >= total) {
            return respondWithData(asdu, new CmsInt32U(total));
        }

        int end = Math.min(start + pageSize, total);
        byte[] nextId = createCallId();
        RpcContinuationState nextState = new RpcContinuationState("iterate", end);

        CmsRpcCall response = new CmsRpcCall(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .rspData(new CmsInt32U(end))
                .nextCallID(nextId);

        session.setAttribute(callIdKey(nextId), nextState);

        log.debug("[Server] RpcCall iterate: {}/{}, nextCallId={}", end, total, bytesToHex(nextId));
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsRpcCall request, int errorCode) {
        CmsRpcCall response = new CmsRpcCall(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }

    private byte[] createCallId() {
        int id = callIdSeq.getAndIncrement();
        return new byte[]{
            (byte) (id >> 24),
            (byte) (id >> 16),
            (byte) (id >> 8),
            (byte) id
        };
    }

    private static Object callIdKey(byte[] callId) {
        return java.nio.ByteBuffer.wrap(callId);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private static class RpcContinuationState {
        final String method;
        final int offset;
        final String echoData;

        RpcContinuationState(String method, int offset) {
            this.method = method;
            this.offset = offset;
            this.echoData = null;
        }

        RpcContinuationState(String method, String echoData) {
            this.method = method;
            this.offset = 0;
            this.echoData = echoData;
        }
    }
}
