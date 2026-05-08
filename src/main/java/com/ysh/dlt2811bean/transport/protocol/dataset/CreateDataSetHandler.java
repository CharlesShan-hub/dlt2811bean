package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDataSetHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(CreateDataSetHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CREATE_DATA_SET;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling CreateDataSet: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsCreateDataSet) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsCreateDataSet asdu = (CmsCreateDataSet) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (asdu.memberData == null || asdu.memberData.size() == 0) {
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

        String afterRef = asdu.referenceAfter.get();
        boolean isAppend = afterRef != null && !afterRef.isEmpty();

        if (isAppend) {
            return handleAppend(asdu, device, afterRef);
        } else {
            return handleCreate(asdu, device, rest);
        }
    }

    private CmsApdu handleCreate(CmsCreateDataSet asdu, SclIED.SclLDevice device, String rest) {
        int dotIdx = rest.indexOf('.');
        String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;

        for (SclIED.SclDataSet existing : device.getLn0().getDataSets()) {
            if (existing.getName().equals(dsName)) {
                log.warn("[Server] CreateDataSet: data set already exists: {}", dsName);
                return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        SclIED.SclDataSet newDs = new SclIED.SclDataSet(dsName);
        newDs.setDesc("dynamically created");
        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclIED.SclFCDA fcda = buildFcda(entry);
            newDs.addFcda(fcda);
        }
        device.getLn0().addDataSet(newDs);

        log.debug("[Server] CreateDataSet: created '{}' with {} members", dsName, asdu.memberData.size());

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private CmsApdu handleAppend(CmsCreateDataSet asdu, SclIED.SclLDevice device, String afterRef) {
        int dotIdx = afterRef.indexOf('.');
        String dsName = dotIdx >= 0 ? afterRef.substring(dotIdx + 1) : afterRef;

        SclIED.SclDataSet existing = null;
        for (SclIED.SclDataSet ds : device.getLn0().getDataSets()) {
            if (ds.getName().equals(dsName)) {
                existing = ds;
                break;
            }
        }

        if (existing == null) {
            log.warn("[Server] CreateDataSet: data set not found for append: {}", dsName);
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclIED.SclFCDA fcda = buildFcda(entry);
            existing.addFcda(fcda);
        }

        log.debug("[Server] CreateDataSet: appended {} members to '{}'", asdu.memberData.size(), dsName);

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private SclIED.SclFCDA buildFcda(CmsCreateDataSetEntry entry) {
        SclIED.SclFCDA fcda = new SclIED.SclFCDA();
        String ref = entry.reference.get();
        if (ref != null) {
            int slashIdx = ref.indexOf('/');
            if (slashIdx >= 0) {
                fcda.setLdInst(ref.substring(0, slashIdx));
                String fcdaRest = ref.substring(slashIdx + 1);
                int dotIdx = fcdaRest.indexOf('.');
                if (dotIdx >= 0) {
                    fcda.setLnClass(fcdaRest.substring(0, dotIdx));
                    fcda.setDoName(fcdaRest.substring(dotIdx + 1));
                } else {
                    fcda.setLnClass(fcdaRest);
                }
            } else {
                fcda.setLnClass(ref);
            }
        }
        if (entry.fc != null) {
            fcda.setFc(entry.fc.get());
        }
        return fcda;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsCreateDataSet request, int errorCode) {
        CmsCreateDataSet response = new CmsCreateDataSet(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
