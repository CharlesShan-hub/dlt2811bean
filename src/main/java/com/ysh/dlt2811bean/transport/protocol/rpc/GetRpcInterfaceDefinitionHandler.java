package com.ysh.dlt2811bean.transport.protocol.rpc;

import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.StructureEntry;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsRpcMethodDefEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetRpcInterfaceDefinitionHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetRpcInterfaceDefinitionHandler.class);

    private final Map<String, List<CmsRpcMethodDefEntry>> builtinInterfaces = new LinkedHashMap<>();

    public GetRpcInterfaceDefinitionHandler() {
        List<CmsRpcMethodDefEntry> if1 = new ArrayList<>();
        if1.add(createEntry("IF1.Method1", 1, 5000,
                CmsDataDefinition.ofVisibleString(128),
                CmsDataDefinition.ofInt32()));
        if1.add(createEntry("IF1.Method2", 2, 10000,
                CmsDataDefinition.ofStructure(List.of(
                    new StructureEntry("param1", CmsDataDefinition.ofInt32()),
                    new StructureEntry("param2", CmsDataDefinition.ofVisibleString(64)))),
                CmsDataDefinition.ofVisibleString(255)));
        builtinInterfaces.put("IF1", if1);

        List<CmsRpcMethodDefEntry> if2 = new ArrayList<>();
        if2.add(createEntry("IF2.status", 1, 3000,
                CmsDataDefinition.ofInt8(),
                CmsDataDefinition.ofStructure(List.of(
                    new StructureEntry("state", CmsDataDefinition.ofInt8U()),
                    new StructureEntry("message", CmsDataDefinition.ofVisibleString(128))))));
        if2.add(createEntry("IF2.reset", 1, 8000,
                CmsDataDefinition.ofInt8(),
                CmsDataDefinition.ofInt8()));
        builtinInterfaces.put("IF2", if2);
    }

    private static CmsRpcMethodDefEntry createEntry(String name, long version, long timeout,
                                                     CmsDataDefinition request,
                                                     CmsDataDefinition response) {
        CmsRpcMethodDefEntry entry = new CmsRpcMethodDefEntry();
        entry.name.set(name);
        entry.version.set(Long.valueOf(version));
        entry.timeout.set(Long.valueOf(timeout));
        entry.request = request;
        entry.response = response;
        return entry;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_RPC_INTERFACE_DEFINITION;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetRpcInterfaceDefinition: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetRpcInterfaceDefinition) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetRpcInterfaceDefinition asdu = (CmsGetRpcInterfaceDefinition) request.getAsdu();
        String ifName = asdu.interfaceName.get();

        if (ifName == null || ifName.isEmpty()) {
            log.warn("[Server] GetRpcInterfaceDefinition: empty interface name");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        List<CmsRpcMethodDefEntry> allMethods = builtinInterfaces.get(ifName);
        if (allMethods == null) {
            log.warn("[Server] GetRpcInterfaceDefinition: interface not found: {}", ifName);
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String after = asdu.referenceAfter.get();
        int startIdx = 0;
        if (after != null && !after.isEmpty()) {
            for (int i = 0; i < allMethods.size(); i++) {
                if (allMethods.get(i).name.get().equals(after)) {
                    startIdx = i + 1;
                    break;
                }
            }
        }

        int pageSize = 5;
        CmsGetRpcInterfaceDefinition response = new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        for (int i = startIdx; i < allMethods.size() && i < startIdx + pageSize; i++) {
            response.method.add(allMethods.get(i).copy());
        }

        if (startIdx + pageSize < allMethods.size()) {
            response.moreFollows.set(true);
        }

        log.debug("[Server] GetRpcInterfaceDefinition: interface={}, entries={}, moreFollows={}",
                ifName, response.method.size(), response.moreFollows.get());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsGetRpcInterfaceDefinition request, int errorCode) {
        CmsGetRpcInterfaceDefinition response = new CmsGetRpcInterfaceDefinition(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
