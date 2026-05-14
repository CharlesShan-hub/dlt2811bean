package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsDeleteDataSet;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class DeleteDataSetHandler extends AbstractCmsServiceHandler<CmsDeleteDataSet> {

    public DeleteDataSetHandler() {
        super(ServiceName.DELETE_DATA_SET, CmsDeleteDataSet::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SclTypeResolver.SclDsRef parsed = SclTypeResolver.parseDsRef(dsRef);
        if (parsed == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclLDevice device = findLDevice(server, parsed.getLdName());
        if (device == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclLN targetLn = SclTypeResolver.findLnInDevice(device, parsed.getLnName());
        if (targetLn == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclDataSet toRemove = null;
        for (SclIED.SclDataSet ds : targetLn.getDataSets()) {
            if (ds.getName().equals(parsed.getDsName())) {
                toRemove = ds;
                break;
            }
        }

        if (toRemove == null) {
            log.warn("[Server] DeleteDataSet: data set not found: {}", dsRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        targetLn.getDataSets().remove(toRemove);
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