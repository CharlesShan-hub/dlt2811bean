// package com.ysh.dlt2811bean.transport.protocol.dataset;

// import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
// import com.ysh.dlt2811bean.datatypes.data.CmsData;
// import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
// import com.ysh.dlt2811bean.datatypes.type.CmsType;
// import com.ysh.dlt2811bean.scl.model.SclIED;
// import com.ysh.dlt2811bean.scl.model.SclIED.SclDAI;
// import com.ysh.dlt2811bean.scl.model.SclIED.SclDOI;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

// public class SetDataSetValuesHandler extends AbstractCmsServiceHandler<CmsSetDataSetValues> {

//     public SetDataSetValuesHandler() {
//         super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValues::new);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {

//         String dsRef = asdu.datasetReference.get();
//         if (dsRef == null || dsRef.isEmpty()) {
//             return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//         }

//         int slashIdx = dsRef.indexOf('/');
//         if (slashIdx < 0) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         String ldName = dsRef.substring(0, slashIdx);
//         String rest = dsRef.substring(slashIdx + 1);

//         SclIED.SclLDevice device = findLDevice(server, ldName);
//         if (device == null) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         SclIED.SclDataSet dataSet = findDataSet(device, rest);
//         if (dataSet == null) {
//             return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
//         }

//         int memberCount = asdu.memberValue.size();
//         if (memberCount == 0) {
//             return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
//                     .reqId(asdu.reqId().get()));
//         }

//         String afterRef = asdu.referenceAfter.get();
//         int startIndex = 0;
//         if (afterRef != null && !afterRef.isEmpty()) {
//             startIndex = findStartIndex(dataSet, afterRef);
//             if (startIndex < 0) {
//                 return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
//             }
//         }

//         int availableMembers = dataSet.getFcdaList().size() - startIndex;
//         if (memberCount > availableMembers) {
//             log.warn("[Server] SetDataSetValues: memberCount={} exceeds available={} after index={}",
//                     memberCount, availableMembers, startIndex);
//         }

//         CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
//         boolean hasAnyError = false;

//         for (int i = 0; i < memberCount; i++) {
//             int fcdaIndex = startIndex + i;
//             if (fcdaIndex >= dataSet.getFcdaList().size()) {
//                 results.add(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
//                 hasAnyError = true;
//             } else {
//                 SclIED.SclFCDA fcda = dataSet.getFcdaList().get(fcdaIndex);
//                 int error = setValueOnFcda(server, ldName, fcda, asdu.memberValue.get(i));
//                 results.add(new CmsServiceError(error));
//                 if (error != CmsServiceError.NO_ERROR) {
//                     hasAnyError = true;
//                 }
//             }
//         }

//         if (hasAnyError) {
//             CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
//                     .reqId(asdu.reqId().get());
//             response.result = results;
//             return new CmsApdu(response);
//         }

//         return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get()));
//     }

//     private int setValueOnFcda(SclIED.SclServer server, String ldName, SclIED.SclFCDA fcda, CmsData<?> data) {
//         String doName = fcda.getDoName();
//         if (doName == null || doName.isEmpty()) {
//             return CmsServiceError.INSTANCE_NOT_AVAILABLE;
//         }

//         String lnName = buildLnName(fcda);
//         SclIED.SclLDevice device = findLDevice(server, ldName);
//         if (device == null) {
//             return CmsServiceError.INSTANCE_NOT_AVAILABLE;
//         }

//         SclDOI doi = findDoiInDevice(device, lnName, doName);
//         if (doi == null) {
//             return CmsServiceError.INSTANCE_NOT_AVAILABLE;
//         }

//         String newValue = extractValue(data);
//         if (newValue == null) {
//             return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
//         }

//         String daName = fcda.getDaName();
//         if (daName != null && !daName.isEmpty()) {
//             // FCDA has explicit daName — find or create DAI
//             SclDAI dai = findDaiByName(doi.getDais(), daName);
//             if (dai == null) {
//                 dai = new SclDAI();
//                 dai.setName(daName);
//                 doi.getDais().add(dai);
//             }
//             dai.setValue(newValue);
//             log.debug("[Server] SetDataSetValues: {} = {}", buildFcdaRef(fcda), newValue);
//         } else {
//             // DO-level FCDA — find first DAI with a value or create a default one
//             SclDAI dai = null;
//             for (SclDAI d : doi.getDais()) {
//                 dai = d;
//                 break;
//             }
//             if (dai == null) {
//                 dai = new SclDAI();
//                 dai.setName("stVal");
//                 doi.getDais().add(dai);
//             }
//             dai.setValue(newValue);
//             log.debug("[Server] SetDataSetValues: {} = {}", buildFcdaRef(fcda), newValue);
//         }

//         return CmsServiceError.NO_ERROR;
//     }

//     private String buildLnName(SclIED.SclFCDA fcda) {
//         StringBuilder sb = new StringBuilder();
//         if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
//             sb.append(fcda.getPrefix());
//         }
//         sb.append(fcda.getLnClass());
//         if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
//             sb.append(fcda.getLnInst());
//         }
//         return sb.toString();
//     }

//     private String extractValue(CmsData<?> data) {
//         if (data == null) return null;
//         CmsType<?> inner = data.getInnerValue();
//         if (inner == null) return null;
//         if (inner instanceof com.ysh.dlt2811bean.datatypes.string.CmsUtf8String) {
//             return ((com.ysh.dlt2811bean.datatypes.string.CmsUtf8String) inner).get();
//         }
//         if (inner instanceof com.ysh.dlt2811bean.datatypes.string.CmsVisibleString) {
//             return ((com.ysh.dlt2811bean.datatypes.string.CmsVisibleString) inner).get();
//         }
//         return inner.toString();
//     }

//     private SclDAI findDaiByName(java.util.List<SclDAI> dais, String name) {
//         for (SclDAI dai : dais) {
//             if (dai.getName().equals(name)) {
//                 return dai;
//             }
//         }
//         return null;
//     }

//     private SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
//         if (device.getLn0() != null) {
//             String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
//             if (ln0Name.equals(lnName)) {
//                 for (SclDOI doi : device.getLn0().getDois()) {
//                     if (doi.getName().equals(doName)) return doi;
//                 }
//                 return null;
//             }
//         }
//         for (SclIED.SclLN ln : device.getLns()) {
//             String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
//                     ? ln.getLnClass() + ln.getInst()
//                     : ln.getPrefix() + ln.getLnClass() + ln.getInst();
//             if (curLnName.equals(lnName)) {
//                 for (SclDOI doi : ln.getDois()) {
//                     if (doi.getName().equals(doName)) return doi;
//                 }
//                 return null;
//             }
//         }
//         return null;
//     }

//     private int findStartIndex(SclIED.SclDataSet dataSet, String afterRef) {
//         for (int i = 0; i < dataSet.getFcdaList().size(); i++) {
//             SclIED.SclFCDA fcda = dataSet.getFcdaList().get(i);
//             String memberRef = buildFcdaRef(fcda);
//             if (memberRef.equals(afterRef)) {
//                 return i + 1;
//             }
//         }
//         return -1;
//     }

//     private String buildFcdaRef(SclIED.SclFCDA fcda) {
//         StringBuilder sb = new StringBuilder();
//         sb.append(fcda.getLdInst()).append("/");
//         if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
//             sb.append(fcda.getPrefix());
//         }
//         sb.append(fcda.getLnClass());
//         if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
//             sb.append(fcda.getLnInst());
//         }
//         sb.append(".").append(fcda.getDoName());
//         if (fcda.getDaName() != null && !fcda.getDaName().isEmpty()) {
//             sb.append(".").append(fcda.getDaName());
//         }
//         return sb.toString();
//     }

//     private SclIED.SclDataSet findDataSet(SclIED.SclLDevice device, String ref) {
//         int dotIdx = ref.indexOf('.');
//         if (dotIdx < 0) {
//             if (device.getLn0() == null) return null;
//             for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
//                 if (ds.getName().equals(ref)) {
//                     return ds;
//                 }
//             }
//             return null;
//         }
//         String lnName = ref.substring(0, dotIdx);
//         String dsName = ref.substring(dotIdx + 1);
//         if (device.getLn0() != null) {
//             String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
//             if (ln0Name.equals(lnName)) {
//                 for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
//                     if (ds.getName().equals(dsName)) {
//                         return ds;
//                     }
//                 }
//                 return null;
//             }
//         }
//         for (SclIED.SclLN ln : device.getLns()) {
//             String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
//                     ? ln.getLnClass() + ln.getInst()
//                     : ln.getPrefix() + ln.getLnClass() + ln.getInst();
//             if (curLnName.equals(lnName)) {
//                 for (SclIED.SclDataSet ds : ln.getDataSets()) {
//                     if (ds.getName().equals(dsName)) {
//                         return ds;
//                     }
//                 }
//                 return null;
//             }
//         }
//         return null;
//     }

//     private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
//         for (SclIED.SclLDevice device : server.getLDevices()) {
//             if (device.getInst().equals(ldName)) {
//                 return device;
//             }
//         }
//         return null;
//     }

//     @Override
//     protected CmsApdu buildNegativeResponse(int errorCode) {
//         CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
//                 .reqId(request.getReqId())
//                 .addResult(errorCode);
//         return new CmsApdu(response);
//     }
// }