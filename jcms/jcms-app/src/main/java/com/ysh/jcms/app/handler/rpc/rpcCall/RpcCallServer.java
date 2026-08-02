package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.rpc.RpcRegistry;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.rpc.CmsRpcCallError;
import com.ysh.jcms.pdu.rpc.CmsRpcCallRequest;
import com.ysh.jcms.pdu.rpc.CmsRpcCallResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RpcCallServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(RpcCallServer.class);

    /** Registered method implementations. */
    private static final Map<String, Function<Void, String>> methods = new HashMap<>();

    static {
        methods.put("SystemInfo.getServerVersion", v -> "DL/T 2811 CMS Server v1.0.0");
    }

    public RpcCallServer() {
        super(ServiceName.RPC_CALL, CmsRpcCallRequest.class, CmsRpcCallError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsRpcCallRequest req = (CmsRpcCallRequest) rawReq;
        String method = str(req.method);
        if (method == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Ensure the method exists in registry
        if (RpcRegistry.getMethodByRef(method) == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        // Dispatch to implementation
        Function<Void, String> impl = methods.get(method);
        if (impl == null) {
            log.warn("RpcCall from {}: method='{}' has no implementation", session.getSessionId(), method);
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String result = impl.apply(null);
        log.info("RpcCall from {}: method='{}' -> '{}'", session.getSessionId(), method, result);

        // Build response with visible-string data
        CmsData rspData = new CmsData().alt_visible_string(result);

        CmsRpcCallResponse resp = new CmsRpcCallResponse().rspData(rspData);
        return ok(resp, reqId);
    }
}
