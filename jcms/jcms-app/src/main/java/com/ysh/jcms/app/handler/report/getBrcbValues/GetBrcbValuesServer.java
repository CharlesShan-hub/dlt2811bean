package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsRcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesError;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetBrcbValuesServer extends BaseServerHandler<CmsGetBrcbValuesRequest, CmsGetBrcbValuesError> {

    public GetBrcbValuesServer() {
        super(ServiceName.GET_BRCB_VALUES, CmsGetBrcbValuesRequest.class, CmsGetBrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetBrcbValuesRequest req, int reqId) {
        log.info("GetBRCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetBrcbValuesResponse resp = new CmsGetBrcbValuesResponse();

        for (int i = 0; i < req.reference.size(); i++) {
            String ref = str(req.reference.get(i));
            CmsRcbValueChoice choice = new CmsRcbValueChoice();
            CmsBrcb brcb = SclControlBlockService.resolveBrcb(ied, ap, ref);
            if (brcb != null) {
                choice.altValue(brcb);
            } else {
                choice.altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.brcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetBRCBValues: returning {} entries", resp.brcb.size());
        return ok(resp, reqId);
    }
}
