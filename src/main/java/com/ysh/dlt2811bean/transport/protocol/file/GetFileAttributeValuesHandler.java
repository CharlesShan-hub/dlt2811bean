package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetFileAttributeValues service (SC=0x83).
 */
public class GetFileAttributeValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE_ATTRIBUTE_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetFileAttributeValues asdu = (CmsGetFileAttributeValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
