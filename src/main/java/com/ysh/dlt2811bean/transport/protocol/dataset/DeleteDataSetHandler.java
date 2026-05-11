package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsDeleteDataSet;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class DeleteDataSetHandler extends AbstractCmsServiceHandler<CmsDeleteDataSet> {

    public DeleteDataSetHandler() {
        super(ServiceName.DELETE_DATA_SET, CmsDeleteDataSet::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsDeleteDataSet asdu = (CmsDeleteDataSet) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (device.getLn0() == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
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
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
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

}
