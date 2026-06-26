package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.svc.directory.CmsObjectClass;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class SvrDirServer extends BaseServerHandler {

    public SvrDirServer() {
        super(ServiceName.GET_SERVER_DIRECTORY);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsGetServerDirectoryRequest req = new CmsGetServerDirectoryRequest();
        if (!tryDecode(session, request, req)) {
            return buildDirError(request.reqId(), CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}",
            session.getSessionId(), reqId, req.objectClass.value());

        if (req.objectClass.value() != CmsObjectClass.LOGICAL_DEVICE) {
            return buildDirError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        SclServer server = getSclServer(session);
        if (server == null) {
            return buildDirError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        List<String> ldNames = server.getLDeviceNames(refAfter);
        if (ldNames == null) {
            return buildDirError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse()
            .reqId(reqId);

        for (String name : ldNames) {
            resp.reference.add(new CmsObjectReference(name));
        }
        resp.moreFollows(false);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetServerDirectoryResponse", e);
            return buildDirError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private Frame buildDirError(int reqId, int errorCode) {
        return buildError(new CmsGetServerDirectoryError()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode(), reqId);
    }
}
