package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetRpcInterfaceDirectoryHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetRpcInterfaceDirectoryHandler.class);

    private static final List<String> INTERFACES = List.of("IF1", "IF2");

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_INTERFACE_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetRpcInterfaceDirectory: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetRpcInterfaceDirectory) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetRpcInterfaceDirectory asdu = (CmsGetRpcInterfaceDirectory) request.getAsdu();

        String after = asdu.referenceAfter.get();
        int startIdx = 0;
        if (after != null && !after.isEmpty()) {
            int idx = INTERFACES.indexOf(after);
            if (idx >= 0) {
                startIdx = idx + 1;
            }
        }

        CmsGetRpcInterfaceDirectory response = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        for (int i = startIdx; i < INTERFACES.size(); i++) {
            response.addReference(INTERFACES.get(i));
        }

        log.debug("[Server] GetRpcInterfaceDirectory: {} entries", response.reference.size());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsGetRpcInterfaceDirectory request, int errorCode) {
        CmsGetRpcInterfaceDirectory response = new CmsGetRpcInterfaceDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
