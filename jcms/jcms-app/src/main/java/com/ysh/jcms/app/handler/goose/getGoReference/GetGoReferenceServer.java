package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.svc.goose.CmsGetGoReferenceError;
import com.ysh.jcms.svc.goose.CmsGetGoReferenceRequest;
import com.ysh.jcms.svc.goose.CmsGetGoReferenceResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetGoReference server handler.
 *
 * <p>
 * Given a GoCB reference and member offsets, resolves each offset to the
 * corresponding (reference, fc) pair from the GoCB's dataset.
 */
public class GetGoReferenceServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetGoReferenceServer.class);

    public GetGoReferenceServer() {
        super(ServiceName.GET_GO_REFERENCE, CmsGetGoReferenceRequest.class, CmsGetGoReferenceError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetGoReferenceRequest req = (CmsGetGoReferenceRequest) rawReq;
        String gocbRef = str(req.gocbReference);
        log.info("GetGoReference from {}: reqId={}, gocbRef={}, {} offsets", session.getSessionId(), reqId, gocbRef, req.memberOfs.count);

        // For now, return a basic response with the GoCB reference and confRev.
        // Full dataset member resolution would require traversing the SCL data model
        // to map each offset to its (reference, fc) pair.

        CmsGetGoReferenceResponse resp = new CmsGetGoReferenceResponse().reqId(reqId).gocbReference(str(req.gocbReference)).confRev(0);

        // memberData is left empty — dataset resolution not yet implemented
        log.info("GetGoReference: returning gocbRef={} (dataset resolution TBD)", gocbRef);
        return ok(resp, reqId);
    }
}
