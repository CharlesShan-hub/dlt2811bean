// package com.ysh.dlt2811bean.transport.protocol.dataset;

// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.scl.SclTypeResolver;
// import com.ysh.dlt2811bean.scl.model.SclIED;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
// import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

// public class CreateDataSetHandler extends AbstractCmsServiceHandler<CmsCreateDataSet> {

//     public CreateDataSetHandler() {
//         super(ServiceName.CREATE_DATA_SET, CmsCreateDataSet::new);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {

//         String dsRef = asdu.datasetReference.get();
//         if (dsRef == null || dsRef.isEmpty()) {
//             return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//         }

//         if (asdu.memberData == null || asdu.memberData.size() == 0) {
//             return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//         }

//         SclTypeResolver.SclDsRef parsed = SclTypeResolver.parseDsRef(dsRef);
//         if (parsed == null) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         SclIED.SclLDevice device = findLDevice(server, parsed.getLdName());
//         if (device == null) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         SclIED.SclLN targetLn = SclTypeResolver.findLnInDevice(device, parsed.getLnName());
//         if (targetLn == null) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         String afterRef = asdu.referenceAfter.get();
//         boolean isAppend = afterRef != null && !afterRef.isEmpty();

//         if (isAppend) {
//             return handleAppend(targetLn, parsed.getDsName(), afterRef);
//         } else {
//             return handleCreate(targetLn, parsed.getDsName());
//         }
//     }

//     private CmsApdu handleCreate(SclIED.SclLN targetLn, String dsName) {
//         for (SclIED.SclDataSet existing : targetLn.getDataSets()) {
//             if (existing.getName().equals(dsName)) {
//                 log.warn("[Server] CreateDataSet: data set already exists: {}", dsName);
//                 return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//             }
//         }

//         SclIED.SclDataSet newDs = new SclIED.SclDataSet(dsName);
//         newDs.setDesc("dynamically created");
//         for (CmsCreateDataSetEntry entry : asdu.memberData) {
//             SclIED.SclFCDA fcda = buildFcda(entry, server);
//             if (fcda != null) {
//                 newDs.addFcda(fcda);
//             }
//         }
//         targetLn.addDataSet(newDs);

//         log.debug("[Server] CreateDataSet: created '{}' with {} members", dsName, asdu.memberData.size());

//         return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get()));
//     }

//     private CmsApdu handleAppend(SclIED.SclLN targetLn, String dsName, String afterRef) {
//         SclIED.SclDataSet existing = null;
//         for (SclIED.SclDataSet ds : targetLn.getDataSets()) {
//             if (ds.getName().equals(dsName)) {
//                 existing = ds;
//                 break;
//             }
//         }

//         if (existing == null) {
//             log.warn("[Server] CreateDataSet: data set not found for append: {}", dsName);
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         for (CmsCreateDataSetEntry entry : asdu.memberData) {
//             SclIED.SclFCDA fcda = buildFcda(entry, server);
//             if (fcda != null) {
//                 existing.addFcda(fcda);
//             }
//         }

//         log.debug("[Server] CreateDataSet: appended {} members to '{}'", asdu.memberData.size(), dsName);

//         return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get()));
//     }

//     private SclIED.SclFCDA buildFcda(CmsCreateDataSetEntry entry, SclIED.SclServer server) {
//         String ref = entry.reference.get();
//         if (ref == null) return null;
//         SclIED.SclFCDA fcda = SclTypeResolver.parseRefToFcda(server, ref);
//         if (fcda == null) {
//             log.warn("[Server] CreateDataSet: cannot resolve reference: {}", ref);
//             return null;
//         }
//         if (entry.fc != null) {
//             fcda.setFc(entry.fc.get());
//         }
//         return fcda;
//     }

//     private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
//         for (SclIED.SclLDevice device : server.getLDevices()) {
//             if (device.getInst().equals(ldName)) {
//                 return device;
//             }
//         }
//         return null;
//     }
// }