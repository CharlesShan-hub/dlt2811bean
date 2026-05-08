package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsDeleteDataSet;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteDataSetHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(DeleteDataSetHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.DELETE_DATA_SET;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling DeleteDataSet: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsDeleteDataSet) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsDeleteDataSet asdu = (CmsDeleteDataSet) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (device.getLn0() == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        int dotIdx = rest.indexOf('.');
        String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;

        SclIED.SclDataSet toRemove = null;
        for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
            if (ds.getName().equals(dsName)) {
                toRemove = ds;
                break;
            }
        }

        if (toRemove == null) {
            log.warn("[Server] DeleteDataSet: data set not found: {}", dsRef);
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        device.getLn0().getDataSets().remove(toRemove);
        log.debug("[Server] DeleteDataSet: removed '{}'", dsRef);

        return new CmsApdu(new CmsDeleteDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsDeleteDataSet request, int errorCode) {
        CmsDeleteDataSet response = new CmsDeleteDataSet(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
