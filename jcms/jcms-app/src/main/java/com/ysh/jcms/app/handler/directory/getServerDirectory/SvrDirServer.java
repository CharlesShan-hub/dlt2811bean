package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.core.pdu.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.core.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.core.data.enumerate.CmsObjectClass;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclDirectoryService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SvrDirServer extends BaseServerHandler<CmsGetServerDirectoryRequest, CmsGetServerDirectoryError> {

    public SvrDirServer() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectoryRequest.class, CmsGetServerDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetServerDirectoryRequest req, int reqId) {
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}, refAfter={}, present={}", session.sessionId(), reqId,
                req.getObjectClass(), req.isPresent("referenceAfter") ? req.referenceAfter.value() : null, req.isPresent("referenceAfter"));

        if (req.getObjectClass() != CmsObjectClass.LOGICAL_DEVICE)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);
        List<String> ldNames = SclDirectoryService.getServerDirectory(ap);

        List<String> afterList = after(ldNames, req.isPresent("referenceAfter") ? req.referenceAfter.value() : null, reqId);

        int ps = pageSize();
        boolean more = afterList.size() > ps;
        int limit = more ? ps : afterList.size();

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse();
        for (int i = 0; i < limit; i++)
            resp.reference.add(new CmsObjectReference(afterList.get(i)));
        resp.moreFollows(more);
        log.info("GetServerDirectory: returning {} entries (pageSize={}, moreFollows={})", limit, ps, more);
        return ok(resp, reqId);
    }
}
