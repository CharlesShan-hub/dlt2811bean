package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberRequest;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * GetGOOSEElementNumber server handler.
 *
 * <p>
 * Given a GoCB reference and member (reference, fc) pairs, finds the offset of
 * each member in the GoCB's dataset. The inverse of GetGoReference.
 */
public class GetGooseElementNumberServer extends BaseServerHandler {

    public GetGooseElementNumberServer() {
        super(ServiceName.GET_GOOSE_ELEMENT_NUMBER, CmsGetGooseElementNumberRequest.class, CmsGetGooseElementNumberError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetGooseElementNumberRequest req = (CmsGetGooseElementNumberRequest) rawReq;
        String gocbRef = str(req.gocbReference);
        log.info("GetGOOSEElementNumber from {}: reqId={}, gocbRef={}, {} members", session.getSessionId(), reqId, gocbRef,
                req.memberData.size());

        // Return the GoCB reference and confRev.
        // Full member offset resolution would require traversing the SCL data model
        // to find each (reference, fc) pair's index in the dataset.

        CmsGetGooseElementNumberResponse resp = new CmsGetGooseElementNumberResponse().gocbReference(str(req.gocbReference)).confRev(0);

        // memberOffset is left empty — dataset resolution not yet implemented
        log.info("GetGOOSEElementNumber: returning gocbRef={} (dataset resolution TBD)", gocbRef);
        return ok(resp, reqId);
    }
}
