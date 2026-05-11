package com.ysh.dlt2811bean.transport.protocol.directory;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;

/**
 * Handler for GetLogicNodeDirectory service (SC=0x52).
 */
public class GetLogicNodeDirectoryHandler extends AbstractCmsServiceHandler<CmsGetLogicalNodeDirectory> {

    public GetLogicNodeDirectoryHandler() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectory::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        return new CmsApdu(request.getAsdu());
    }
}
