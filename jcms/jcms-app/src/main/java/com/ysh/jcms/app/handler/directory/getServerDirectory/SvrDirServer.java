package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.data.enumerate.CmsObjectClass;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SvrDirServer extends BaseServerHandler {

    public SvrDirServer() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectoryRequest.class, CmsGetServerDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetServerDirectoryRequest req = (CmsGetServerDirectoryRequest) rawReq;
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}", session.getSessionId(), reqId, req.objectClass.value());

        if (req.objectClass.value() != CmsObjectClass.LOGICAL_DEVICE)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        SclDocument doc = requireScl(session, reqId);

        List<String> ldNames = after(doc.ldNames(), opt(req.refAfterPresent, req.refAfter), reqId);

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse().reqId(reqId);
        for (String name : ldNames)
            resp.reference.add(new CmsObjectReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }
}
