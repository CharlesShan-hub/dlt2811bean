package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetDataDefinition service (SC=0x33).
 */
public class GetDataDefinitionHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_DEFINITION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetDataDefinition asdu = (CmsGetDataDefinition) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
