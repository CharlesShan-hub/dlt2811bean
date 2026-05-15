// package com.ysh.dlt2811bean.transport.protocol.report;

// import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
// import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.scl.model.SclIED;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
// import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

// public class GetBRCBValuesHandler extends AbstractCmsServiceHandler<CmsGetBRCBValues> {

//     static final java.util.concurrent.ConcurrentHashMap<String, Boolean> rptEnaState = new java.util.concurrent.ConcurrentHashMap<>();

//     public GetBRCBValuesHandler() {
//         super(ServiceName.GET_BRCB_VALUES, CmsGetBRCBValues::new);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {

//         CmsArray<CmsErrorBrcbChoice> choices = new CmsArray<>(CmsErrorBrcbChoice::new);

//         for (int i = 0; i < asdu.brcbReference.size(); i++) {
//             String ref = asdu.brcbReference.get(i).get();
//             CmsErrorBrcbChoice choice = buildBrcbChoice(ref);
//             choices.add(choice);
//         }

//         CmsGetBRCBValues response = new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get());
//         response.errorBrcb = choices;
//         response.moreFollows.set(false);

//         log.debug("[Server] GetBRCBValues: {} references", asdu.brcbReference.size());
//         return new CmsApdu(response);
//     }

//     private CmsErrorBrcbChoice buildBrcbChoice(String ref) {
//         CmsErrorBrcbChoice choice = new CmsErrorBrcbChoice();

//         if (ref == null || ref.isEmpty()) {
//             choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//             return choice;
//         }

//         for (SclIED.SclLDevice ld : server.getLDevices()) {
//             if (ld.getLn0() == null) continue;
//             for (SclIED.SclReportControl rc : ld.getLn0().getReportControls()) {
//                 if (!rc.isBuffered()) continue;
//                 String rcRef = ld.getInst() + "/LLN0." + rc.getName();
//                 if (rcRef.equals(ref)) {
//                     CmsBRCB brcb = new CmsBRCB();
//                     brcb.brcbName.set(rc.getName());
//                     brcb.brcbRef.set(rcRef);
//                     if (rc.getRptID() != null) {
//                         brcb.rptID.set(rc.getRptID());
//                     }
//                     if (rc.getDatSet() != null) {
//                         brcb.datSet.set(rc.getDatSet());
//                     }
//                     if (rc.getConfRev() != null) {
//                         brcb.confRev.set(Long.parseLong(rc.getConfRev()));
//                     }
//                     brcb.rptEna.set(rptEnaState.getOrDefault(rcRef, false));
//                     brcb.purgeBuf.set(false);
//                     brcb.gi.set(false);
//                     choice.selectBrcb().brcb = brcb;
//                     return choice;
//                 }
//             }
//         }

//         choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         return choice;
//     }
// }