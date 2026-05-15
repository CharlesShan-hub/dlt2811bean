package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.*;
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

        SclDataSet toRemove = targetLn.findDataSetByName(dsName);

        if (toRemove == null) {
            log.warn("[Server] DeleteDataSet: data set not found: {}", dsRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        targetLn.getDataSets().remove(toRemove);
        log.debug("[Server] DeleteDataSet: removed '{}'", dsRef);

        return new CmsApdu(new CmsDeleteDataSet(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }
}