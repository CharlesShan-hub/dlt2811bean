package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsSubReference;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclDirectoryService;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class LdDirServer extends BaseServerHandler<CmsGetLogicalDeviceDirectoryRequest, CmsGetLogicalDeviceDirectoryError> {

    public LdDirServer() {
        super(CmsServiceInfo.GET_LOGIC_DEVICE_DIRECTORY, CmsGetLogicalDeviceDirectoryRequest.class,
                CmsGetLogicalDeviceDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLogicalDeviceDirectoryRequest req, int reqId) {
        String ldName = req.isPresent("ldName") ? req.ldName.value() : null;
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        log.info("GetLogicalDeviceDirectory from {}: reqId={}, ldName={}, referenceAfter={}", session.sessionId(), reqId, ldName, refAfter);

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        List<String> lnNames = SclDirectoryService.getLogicalDeviceDirectory(ap, ldName);
        if (lnNames == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        lnNames = after(lnNames, refAfter, reqId);

        // 分页：单次响应不超过 pageSize 条，moreFollows 提示客户端续拉
        int pageSize = pageSize();
        boolean more = lnNames.size() > pageSize;
        int limit = more ? pageSize : lnNames.size();

        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse();
        for (int i = 0; i < limit; i++)
            resp.lnReference.add(new CmsSubReference(lnNames.get(i)));
        resp.moreFollows(more);
        return ok(resp, reqId);
    }

}
