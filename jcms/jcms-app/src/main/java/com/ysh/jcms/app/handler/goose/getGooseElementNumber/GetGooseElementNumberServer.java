package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberRequest;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * GetGOOSEElementNumber server handler.
 *
 * <p>
 * Given a GoCB reference and member (reference, fc) pairs, finds the offset of
 * each member in the GoCB's dataset. The inverse of GetGoReference.
 */
public class GetGooseElementNumberServer extends BaseServerHandler<CmsGetGooseElementNumberRequest, CmsGetGooseElementNumberError> {

    public GetGooseElementNumberServer() {
        super(CmsServiceInfo.GET_GOOSE_ELEMENT_NUMBER, CmsGetGooseElementNumberRequest.class, CmsGetGooseElementNumberError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetGooseElementNumberRequest req, int reqId) {
        String gocbRef = str(req.gocbReference);
        log.info("GetGOOSEElementNumber from {}: reqId={}, gocbRef={}, {} members", session.sessionId(), reqId, gocbRef,
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
