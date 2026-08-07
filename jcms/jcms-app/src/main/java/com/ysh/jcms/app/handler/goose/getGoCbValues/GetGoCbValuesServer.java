package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsGocbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsGoCb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesError;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesRequest;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetGoCbValuesServer extends BaseServerHandler<CmsGetGoCbValuesRequest, CmsGetGoCbValuesError> {

    public GetGoCbValuesServer() {
        super(ServiceName.GET_GOCB_VALUES, CmsGetGoCbValuesRequest.class, CmsGetGoCbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetGoCbValuesRequest req, int reqId) {
        log.info("GetGoCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetGoCbValuesResponse resp = new CmsGetGoCbValuesResponse();

        for (int i = 0; i < req.reference.size(); i++) {
            String ref = str(req.reference.get(i));
            CmsGocbValueChoice choice = new CmsGocbValueChoice();
            CmsGoCb gocb = SclControlBlockService.resolveGocb(ied, ap, ref);
            if (gocb != null) {
                choice.altValue(gocb);
            } else {
                choice.altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.gocb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetGoCBValues: returning {} entries", resp.gocb.size());
        return ok(resp, reqId);
    }
}
