// package com.ysh.dlt2811bean.transport.protocol.log;

// import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.scl.model.SclIED;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogAfter;
// import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

// public class QueryLogAfterHandler extends AbstractCmsServiceHandler<CmsQueryLogAfter> {

//     public QueryLogAfterHandler() {
//         super(ServiceName.QUERY_LOG_AFTER, CmsQueryLogAfter::new);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {

//         String logRef = asdu.logReference.get();
//         if (logRef == null || logRef.isEmpty()) {
//             return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//         }

//         if (!validateLogReference(logRef)) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         log.debug("[Server] QueryLogAfter: logRef={}, startPresent={}", logRef, asdu.startTime != null);

//         CmsQueryLogAfter response = new CmsQueryLogAfter(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get());
//         response.logEntry = new CmsArray<>(CmsLogEntry::new);
//         response.moreFollows.set(false);

//         return new CmsApdu(response);
//     }

//     private boolean validateLogReference(String logRef) {
//         for (SclIED.SclLDevice ld : server.getLDevices()) {
//             if (ld.getLn0() == null) continue;
//             for (SclIED.SclLogControl lc : ld.getLn0().getLogControls()) {
//                 String lcRef = ld.getInst() + "/LLN0." + lc.getName();
//                 if (lcRef.equals(logRef)) {
//                     return true;
//                 }
//             }
//         }
//         return false;
//     }
// }
