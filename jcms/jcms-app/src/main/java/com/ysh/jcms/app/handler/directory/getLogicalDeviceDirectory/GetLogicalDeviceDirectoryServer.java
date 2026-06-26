package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetLogicalDeviceDirectoryServer extends BaseServerHandler {

    public GetLogicalDeviceDirectoryServer() {
        super(ServiceName.GET_LOGIC_DEVICE_DIRECTORY);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsGetLogicalDeviceDirectoryRequest req = new CmsGetLogicalDeviceDirectoryRequest();
        if (!tryDecode(session, request, req)) {
            return buildDirError(request.reqId(), CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        String ldName = req.ldNamePresent.value() && req.ldName.len > 0
            ? new String(req.ldName.value(), StandardCharsets.UTF_8) : null;
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        log.info("GetLogicalDeviceDirectory from {}: reqId={}, ldName={}",
            session.getSessionId(), reqId, ldName);

        SclServer server = getSclServer(session);
        if (server == null) {
            return buildDirError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<String> lnNames;
        if (ldName != null) {
            SclLDevice device = server.findLDeviceByInst(ldName);
            if (device == null) {
                return buildDirError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            lnNames = device.getLnNames(refAfter);
        } else {
            lnNames = server.getAllLnNames(refAfter);
        }

        if (lnNames == null) {
            return buildDirError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse()
            .reqId(reqId);

        for (String name : lnNames) {
            resp.lnReference.add(new CmsSubReference(name));
        }
        resp.moreFollows(false);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetLogicalDeviceDirectoryResponse", e);
            return buildDirError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private Frame buildDirError(int reqId, int errorCode) {
        return buildError(new CmsGetLogicalDeviceDirectoryError()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode(), reqId);
    }
}