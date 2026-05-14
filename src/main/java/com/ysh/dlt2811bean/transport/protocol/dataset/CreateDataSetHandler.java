package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class CreateDataSetHandler extends AbstractCmsServiceHandler<CmsCreateDataSet> {

    public CreateDataSetHandler() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSet::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsCreateDataSet asdu = (CmsCreateDataSet) request.getAsdu();

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (asdu.memberData == null || asdu.memberData.size() == 0) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (device.getLn0() == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String afterRef = asdu.referenceAfter.get();
        boolean isAppend = afterRef != null && !afterRef.isEmpty();

        if (isAppend) {
            return handleAppend(request, asdu, device, afterRef);
        } else {
            return handleCreate(request, asdu, device, rest);
        }
    }

    private CmsApdu handleCreate(CmsApdu request, CmsCreateDataSet asdu, SclIED.SclLDevice device, String rest) {
        int dotIdx = rest.indexOf('.');
        String dsName = dotIdx >= 0 ? rest.substring(dotIdx + 1) : rest;

        for (SclIED.SclDataSet existing : device.getLn0().getDataSets()) {
            if (existing.getName().equals(dsName)) {
                log.warn("[Server] CreateDataSet: data set already exists: {}", dsName);
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        SclIED.SclDataSet newDs = new SclIED.SclDataSet(dsName);
        newDs.setDesc("dynamically created");
        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclIED.SclFCDA fcda = buildFcda(entry, server);
            if (fcda != null) {
                newDs.addFcda(fcda);
            }
        }
        device.getLn0().addDataSet(newDs);

        log.debug("[Server] CreateDataSet: created '{}' with {} members", dsName, asdu.memberData.size());

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private CmsApdu handleAppend(CmsApdu request, CmsCreateDataSet asdu, SclIED.SclLDevice device, String afterRef) {
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
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclIED.SclFCDA fcda = buildFcda(entry, server);
            if (fcda != null) {
                existing.addFcda(fcda);
            }
        }

        log.debug("[Server] CreateDataSet: appended {} members to '{}'", asdu.memberData.size(), dsName);

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private SclIED.SclFCDA buildFcda(CmsCreateDataSetEntry entry, SclIED.SclServer server) {
        String ref = entry.reference.get();
        if (ref == null) return null;
        SclIED.SclFCDA fcda = SclTypeResolver.parseRefToFcda(server, ref);
        if (fcda == null) {
            log.warn("[Server] CreateDataSet: cannot resolve reference: {}", ref);
            return null;
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

}