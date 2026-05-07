package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for DeleteFile service (SC=0x82).
 */
public class DeleteFileHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.DELETE_FILE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsDeleteFile asdu = (CmsDeleteFile) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
