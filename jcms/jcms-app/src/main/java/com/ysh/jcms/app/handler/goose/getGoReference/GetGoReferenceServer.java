package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceError;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceRequest;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * GetGoReference server handler.
 *
 * <p>
 * Given a GoCB reference and member offsets, resolves each offset to the
 * corresponding (reference, fc) pair from the GoCB's dataset.
 */
public class GetGoReferenceServer extends BaseServerHandler<CmsGetGoReferenceRequest, CmsGetGoReferenceError> {

    public GetGoReferenceServer() {
        super(CmsServiceInfo.GET_GO_REFERENCE, CmsGetGoReferenceRequest.class, CmsGetGoReferenceError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetGoReferenceRequest req, int reqId) {
        String gocbRef = str(req.gocbReference);
        log.info("GetGoReference from {}: reqId={}, gocbRef={}, {} offsets", session.sessionId(), reqId, gocbRef, req.memberOfs.size());

        // For now, return a basic response with the GoCB reference and confRev.
        // Full dataset member resolution would require traversing the SCL data model
        // to map each offset to its (reference, fc) pair.

        CmsGetGoReferenceResponse resp = new CmsGetGoReferenceResponse().gocbReference(gocbRef).confRev(0);

        // memberData is left empty — dataset resolution not yet implemented
        log.info("GetGoReference: returning gocbRef={} (dataset resolution TBD)", gocbRef);
        return ok(resp, reqId);
    }
}
