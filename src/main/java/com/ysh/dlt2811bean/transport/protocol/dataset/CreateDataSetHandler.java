package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;



public class CreateDataSetHandler extends AbstractCmsServiceHandler<CmsCreateDataSet> {

    public CreateDataSetHandler() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSet::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (asdu.memberData == null || asdu.memberData.size() == 0) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String lnName = rest.substring(0, dotIdx);
        String dsName = rest.substring(dotIdx + 1);
        if (lnName.isEmpty() || dsName.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclLN targetLn = device.findLnByFullName(lnName);
        if (targetLn == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String afterRef = asdu.referenceAfter.get();
        boolean isAppend = afterRef != null && !afterRef.isEmpty();

        if (isAppend) {
            return handleAppend(targetLn, dsName, afterRef);
        } else {
            return handleCreate(targetLn, dsName);
        }
    }

    private CmsApdu handleCreate(SclLN targetLn, String dsName) {
        for (SclDataSet existing : targetLn.getDataSets()) {
            if (existing.getName().equals(dsName)) {
                log.warn("[Server] CreateDataSet: data set already exists: {}", dsName);
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        }

        SclDataSet newDs = new SclDataSet();
        newDs.setName(dsName);
        newDs.setDesc("dynamically created");
        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclFCDA fcda = buildFcda(entry, server);
            if (fcda != null) {
                newDs.addFcda(fcda);
            }
        }
        targetLn.addDataSet(newDs);

        log.debug("[Server] CreateDataSet: created '{}' with {} members", dsName, asdu.memberData.size());

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private CmsApdu handleAppend(SclLN targetLn, String dsName, String afterRef) {
        SclDataSet existing = null;
        for (SclDataSet ds : targetLn.getDataSets()) {
            if (ds.getName().equals(dsName)) {
                existing = ds;
                break;
            }
        }

        if (existing == null) {
            log.warn("[Server] CreateDataSet: data set not found for append: {}", dsName);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        for (CmsCreateDataSetEntry entry : asdu.memberData) {
            SclFCDA fcda = buildFcda(entry, server);
            if (fcda != null) {
                existing.addFcda(fcda);
            }
        }

        log.debug("[Server] CreateDataSet: appended {} members to '{}'", asdu.memberData.size(), dsName);

        return new CmsApdu(new CmsCreateDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private SclFCDA buildFcda(CmsCreateDataSetEntry entry, SclServer server) {
        String ref = entry.reference.get();
        if (ref == null) return null;
        SclFCDA fcda = server.parseRefToFcda(ref);
        if (fcda == null) {
            log.warn("[Server] CreateDataSet: cannot resolve reference: {}", ref);
            return null;
        }
        if (entry.fc != null) {
            fcda.setFc(entry.fc.get());
        }
        return fcda;
    }
}