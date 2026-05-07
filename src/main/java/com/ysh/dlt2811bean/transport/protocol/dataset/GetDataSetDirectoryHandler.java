package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetDataSetDirectory service (SC=0x39).
 */
public class GetDataSetDirectoryHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_SET_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetDataSetDirectory asdu = (CmsGetDataSetDirectory) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
