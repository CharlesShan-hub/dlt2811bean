// package com.ysh.dlt2811bean.transport.protocol.rpc;

// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
// import java.util.ArrayList;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;

// public class GetRpcMethodDirectoryHandler extends AbstractCmsServiceHandler<CmsGetRpcMethodDirectory> {

//     private final Map<String, List<String>> builtinInterfaces = new LinkedHashMap<>();

//     public GetRpcMethodDirectoryHandler() {
//         super(ServiceName.GET_RPC_METHOD_DIRECTORY, CmsGetRpcMethodDirectory::new);
//         builtinInterfaces.put("IF1", List.of("IF1.Method1", "IF1.Method2"));
//         builtinInterfaces.put("IF2", List.of("IF2.status", "IF2.reset"));
//     }

//     public GetRpcMethodDirectoryHandler(ServiceName serviceName) {
//         super(serviceName, CmsGetRpcMethodDirectory::new);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {
//         String ifName = asdu.interfaceName.get();

//         List<String> allMethods;
//         if (ifName == null || ifName.isEmpty()) {
//             allMethods = new ArrayList<>();
//             for (List<String> methods : builtinInterfaces.values()) {
//                 allMethods.addAll(methods);
//             }
//         } else {
//             allMethods = builtinInterfaces.get(ifName);
//             if (allMethods == null) {
//                 log.warn("[Server] GetRpcMethodDirectory: interface not found: {}", ifName);
//                 return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//             }
//         }

//         String after = asdu.referenceAfter.get();
//         int startIdx = 0;
//         if (after != null && !after.isEmpty()) {
//             int idx = allMethods.indexOf(after);
//             if (idx >= 0) {
//                 startIdx = idx + 1;
//             }
//         }

//         int pageSize = 10;
//         CmsGetRpcMethodDirectory response = new CmsGetRpcMethodDirectory(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get());

//         for (int i = startIdx; i < allMethods.size() && i < startIdx + pageSize; i++) {
//             response.addReference(allMethods.get(i));
//         }

//         if (startIdx + pageSize < allMethods.size()) {
//             response.moreFollows.set(true);
//         }

//         log.debug("[Server] GetRpcMethodDirectory: interface={}, entries={}, moreFollows={}",
//                 ifName != null ? ifName : "(all)", response.reference.size(), response.moreFollows.get());
//         return new CmsApdu(response);
//     }
// }
