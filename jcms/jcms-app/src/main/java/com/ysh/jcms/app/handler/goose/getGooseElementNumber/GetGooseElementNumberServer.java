package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberRequest;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetGOOSEElementNumber server handler.
 *
 * <p>
 * Given a GoCB reference and member (reference, fc) pairs, finds the offset of
 * each member in the GoCB's dataset. The inverse of GetGoReference.
 */
public class GetGooseElementNumberServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetGooseElementNumberServer.class);

    public GetGooseElementNumberServer() {
        super(ServiceName.GET_GOOSE_ELEMENT_NUMBER, CmsGetGooseElementNumberRequest.class, CmsGetGooseElementNumberError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetGooseElementNumberRequest req = (CmsGetGooseElementNumberRequest) rawReq;
        String gocbRef = str(req.gocbReference);
        log.info("GetGOOSEElementNumber from {}: reqId={}, gocbRef={}, {} members", session.getSessionId(), reqId, gocbRef,
                req.memberData.count);

        // Return the GoCB reference and confRev.
        // Full member offset resolution would require traversing the SCL data model
        // to find each (reference, fc) pair's index in the dataset.

        CmsGetGooseElementNumberResponse resp = new CmsGetGooseElementNumberResponse().reqId(reqId).gocbReference(str(req.gocbReference))
                .confRev(0);

        // memberOffset is left empty — dataset resolution not yet implemented
        log.info("GetGOOSEElementNumber: returning gocbRef={} (dataset resolution TBD)", gocbRef);
        return ok(resp, reqId);
    }
}
