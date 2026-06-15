// package com.ysh.dlt2811bean.transport.protocol.rpc;

// import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
// import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition.StructureEntry;
// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
// import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsErrorMethodChoice;
// import com.ysh.dlt2811bean.service.svc.rpc.datatypes.CmsRpcMethodValue;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;

// public class GetRpcMethodDefinitionHandler extends AbstractCmsServiceHandler<CmsGetRpcMethodDefinition> {

//     private final Map<String, CmsRpcMethodValue> builtinMethods = new LinkedHashMap<>();

//     public GetRpcMethodDefinitionHandler() {
//         super(ServiceName.GET_RPC_METHOD_DEFINITION, CmsGetRpcMethodDefinition::new);
//         builtinMethods.put("IF1.Method1", createMethod(
//                 5000, 1,
//                 CmsDataDefinition.ofVisibleString(128),
//                 CmsDataDefinition.ofInt32()));
//         builtinMethods.put("IF1.Method2", createMethod(
//                 10000, 2,
//                 CmsDataDefinition.ofStructure(List.of(
//                     new StructureEntry("param1", CmsDataDefinition.ofInt32()),
//                     new StructureEntry("param2", CmsDataDefinition.ofVisibleString(64)))),
//                 CmsDataDefinition.ofVisibleString(255)));
//         builtinMethods.put("IF2.status", createMethod(
//                 3000, 1,
//                 CmsDataDefinition.ofInt8(),
//                 CmsDataDefinition.ofStructure(List.of(
//                     new StructureEntry("state", CmsDataDefinition.ofInt8U()),
//                     new StructureEntry("message", CmsDataDefinition.ofVisibleString(128))))));
//         builtinMethods.put("IF2.reset", createMethod(
//                 8000, 1,
//                 CmsDataDefinition.ofInt8(),
//                 CmsDataDefinition.ofInt8()));
//     }

//     @Override
//     protected CmsApdu doServerHandle() {

//         CmsGetRpcMethodDefinition response = new CmsGetRpcMethodDefinition(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get());

//         int pageSize = 5;
//         int processed = 0;

//         for (int i = 0; i < asdu.reference.size(); i++) {
//             if (processed >= pageSize) {
//                 response.moreFollows.set(true);
//                 break;
//             }

//             String ref = asdu.reference.get(i).get();
//             CmsRpcMethodValue method = builtinMethods.get(ref);

//             CmsErrorMethodChoice choice = new CmsErrorMethodChoice();
//             if (method != null) {
//                 choice.selectMethod();
//                 choice.method.timeout.set(method.timeout.get());
//                 choice.method.version.set(method.version.get());
//                 choice.method.request = method.request.copy();
//                 choice.method.response = method.response.copy();
//             } else {
//                 choice.selectError();
//                 choice.error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//             }

//             response.addErrorMethodChoice(choice);
//             processed++;
//         }

//         if (response.errorMethod.size() == 0) {
//             log.warn("[Server] GetRpcMethodDefinition: no references provided");
//             return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//         }

//         log.debug("[Server] GetRpcMethodDefinition: {} entries, moreFollows={}",
//                 response.errorMethod.size(), response.moreFollows.get());

//         return new CmsApdu(response);
//     }

//     private static CmsRpcMethodValue createMethod(long timeout, int version,
//                                                    CmsDataDefinition request,
//                                                    CmsDataDefinition response) {
//         CmsRpcMethodValue m = new CmsRpcMethodValue();
//         m.timeout.set(Long.valueOf(timeout));
//         m.version.set(Long.valueOf(version));
//         m.request = request;
//         m.response = response;
//         return m;
//     }
// }