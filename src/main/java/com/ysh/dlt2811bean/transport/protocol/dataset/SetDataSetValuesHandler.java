package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDataSetValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetDataSetValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_DATA_SET_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetDataSetValues: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetDataSetValues) request.getAsdu()).reqId().get() : 0;
            return buildNegativeResponse(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsSetDataSetValues asdu = (CmsSetDataSetValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclDataSet dataSet = findDataSet(device, rest);
        if (dataSet == null) {
            return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        int memberCount = asdu.memberValue.size();
        if (memberCount == 0) {
            return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        String afterRef = asdu.referenceAfter.get();
        int startIndex = 0;
        if (afterRef != null && !afterRef.isEmpty()) {
            startIndex = findStartIndex(dataSet, afterRef);
            if (startIndex < 0) {
                return buildNegativeResponse(asdu.reqId().get(), CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        int availableMembers = dataSet.getFcdaList().size() - startIndex;
        if (memberCount > availableMembers) {
            log.warn("[Server] SetDataSetValues: memberCount={} exceeds available={} after index={}",
                    memberCount, availableMembers, startIndex);
        }

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(100);
        boolean hasAnyError = false;

        for (int i = 0; i < memberCount; i++) {
            int fcdaIndex = startIndex + i;
            if (fcdaIndex >= dataSet.getFcdaList().size()) {
                results.add(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
                hasAnyError = true;
            } else {
                results.add(new CmsServiceError(CmsServiceError.NO_ERROR));
            }
        }

        if (hasAnyError) {
            CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            return new CmsApdu(response);
        }

        return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private int findStartIndex(SclIED.SclDataSet dataSet, String afterRef) {
        for (int i = 0; i < dataSet.getFcdaList().size(); i++) {
            SclIED.SclFCDA fcda = dataSet.getFcdaList().get(i);
            String memberRef = buildFcdaRef(fcda);
            if (memberRef.equals(afterRef)) {
                return i + 1;
            }
        }
        return -1;
    }

    private String buildFcdaRef(SclIED.SclFCDA fcda) {
        StringBuilder sb = new StringBuilder();
        sb.append(fcda.getLdInst()).append("/");
        if (fcda.getPrefix() != null && !fcda.getPrefix().isEmpty()) {
            sb.append(fcda.getPrefix());
        }
        sb.append(fcda.getLnClass());
        if (fcda.getLnInst() != null && !fcda.getLnInst().isEmpty()) {
            sb.append(fcda.getLnInst());
        }
        sb.append(".").append(fcda.getDoName());
        if (fcda.getDaName() != null && !fcda.getDaName().isEmpty()) {
            sb.append(".").append(fcda.getDaName());
        }
        return sb.toString();
    }

    private SclIED.SclDataSet findDataSet(SclIED.SclLDevice device, String ref) {
        if (device.getLn0() == null) return null;
        int dotIdx = ref.indexOf('.');
        if (dotIdx < 0) return null;
        String dsName = ref.substring(dotIdx + 1);
        for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
            if (ds.getName().equals(dsName)) {
                return ds;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(int reqId, int errorCode) {
        CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId)
                .addResult(errorCode);
        return new CmsApdu(response);
    }
}
