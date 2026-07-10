package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.svc.directory.CmsObjectClass;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SvrDirServer extends BaseServerHandler {

    public SvrDirServer() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectoryRequest.class, CmsGetServerDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetServerDirectoryRequest req = (CmsGetServerDirectoryRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}", session.getSessionId(), reqId, req.objectClass.value());

        if (req.objectClass.value() != CmsObjectClass.LOGICAL_DEVICE)
            return ok(new CmsGetServerDirectoryError().reqId(reqId).serviceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE), reqId);

        SclDocument doc = getScl2Document(session);
        if (doc == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        List<String> ldNames = new ArrayList<>();
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    for (SclLDevice ld : srv.lDevices()) {
                        ldNames.add(ld.inst());
                    }
                }
            }
        }

        if (refAfter != null && !refAfter.isEmpty()) {
            int idx = ldNames.indexOf(refAfter);
            if (idx < 0)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            ldNames = ldNames.subList(idx + 1, ldNames.size());
        }

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse().reqId(reqId);
        for (String name : ldNames)
            resp.reference.add(new CmsObjectReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }
}
