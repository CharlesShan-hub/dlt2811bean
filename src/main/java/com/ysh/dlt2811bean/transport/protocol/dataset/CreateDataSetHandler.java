package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsCreateDataSet;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for CreateDataSet service (SC=0x36).
 */
public class CreateDataSetHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CREATE_DATA_SET;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsCreateDataSet asdu = (CmsCreateDataSet) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
