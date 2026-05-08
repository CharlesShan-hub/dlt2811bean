package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetRpcMethodDirectoryHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetRpcMethodDirectoryHandler.class);

    private final Map<String, List<String>> builtinInterfaces = new LinkedHashMap<>();

    public GetRpcMethodDirectoryHandler() {
        builtinInterfaces.put("IF1", List.of("IF1.Method1", "IF1.Method2"));
        builtinInterfaces.put("IF2", List.of("IF2.status", "IF2.reset"));
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_METHOD_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetRpcMethodDirectory: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetRpcMethodDirectory) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsGetRpcMethodDirectory asdu = (CmsGetRpcMethodDirectory) request.getAsdu();
        String ifName = asdu.interfaceName.get();

        List<String> allMethods;
        if (ifName == null || ifName.isEmpty()) {
            allMethods = new ArrayList<>();
            for (List<String> methods : builtinInterfaces.values()) {
                allMethods.addAll(methods);
            }
        } else {
            allMethods = builtinInterfaces.get(ifName);
            if (allMethods == null) {
                log.warn("[Server] GetRpcMethodDirectory: interface not found: {}", ifName);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        String after = asdu.referenceAfter.get();
        int startIdx = 0;
        if (after != null && !after.isEmpty()) {
            int idx = allMethods.indexOf(after);
            if (idx >= 0) {
                startIdx = idx + 1;
            }
        }

        int pageSize = 10;
        CmsGetRpcMethodDirectory response = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        for (int i = startIdx; i < allMethods.size() && i < startIdx + pageSize; i++) {
            response.addReference(allMethods.get(i));
        }

        if (startIdx + pageSize < allMethods.size()) {
            response.moreFollows.set(true);
        }

        log.debug("[Server] GetRpcMethodDirectory: interface={}, entries={}, moreFollows={}",
                ifName != null ? ifName : "(all)", response.reference.size(), response.moreFollows.get());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsGetRpcMethodDirectory request, int errorCode) {
        CmsGetRpcMethodDirectory response = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
