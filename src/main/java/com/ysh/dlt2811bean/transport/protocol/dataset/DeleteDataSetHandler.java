package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsDeleteDataSet;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for DeleteDataSet service (SC=0x37).
 */
public class DeleteDataSetHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.DELETE_DATA_SET;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsDeleteDataSet asdu = (CmsDeleteDataSet) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
