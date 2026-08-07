package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsUrcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetUrcbValuesServer extends BaseServerHandler<CmsGetUrcbValuesRequest, CmsGetUrcbValuesError> {

    public GetUrcbValuesServer() {
        super(ServiceName.GET_URCB_VALUES, CmsGetUrcbValuesRequest.class, CmsGetUrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetUrcbValuesRequest req, int reqId) {
        log.info("GetURCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetUrcbValuesResponse resp = new CmsGetUrcbValuesResponse();

        for (int i = 0; i < req.reference.size(); i++) {
            String ref = str(req.reference.get(i));
            CmsUrcbValueChoice choice = new CmsUrcbValueChoice();
            CmsUrcb urcb = SclControlBlockService.resolveUrcb(ied, ap, ref);
            if (urcb != null) {
                choice.altValue(urcb);
            } else {
                choice.altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.urcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetURCBValues: returning {} entries", resp.urcb.size());
        return ok(resp, reqId);
    }
}
