package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
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
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetServerDirectoryRequest req = (CmsGetServerDirectoryRequest) rawReq;
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}", session.getSessionId(), reqId, req.getObjectClass());

        if (req.getObjectClass() != CmsObjectClass.LOGICAL_DEVICE)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        SclDocument doc = requireScl(session, reqId);

        List<String> ldNames = after(doc.ldNames(), req.isPresent("referenceAfter") ? req.referenceAfter.value() : null, reqId);

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse();
        for (String name : ldNames)
            resp.reference.add(new CmsObjectReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }
}
