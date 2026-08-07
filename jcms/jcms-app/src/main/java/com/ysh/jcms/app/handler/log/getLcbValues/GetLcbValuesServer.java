package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsLcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetLcbValuesServer extends BaseServerHandler<CmsGetLcbValuesRequest, CmsGetLcbValuesError> {

    public GetLcbValuesServer() {
        super(ServiceName.GET_LCB_VALUES, CmsGetLcbValuesRequest.class, CmsGetLcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLcbValuesRequest req, int reqId) {
        log.info("GetLCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);

        CmsGetLcbValuesResponse resp = new CmsGetLcbValuesResponse();

        for (CmsObjectReference refObj : req.reference) {
            String ref = str(refObj);
            CmsLcbValueChoice choice;
            CmsLcb lcb = SclControlBlockService.resolveLcb(ied, ref);
            if (lcb != null) {
                choice = new CmsLcbValueChoice().altValue(lcb);
            } else {
                choice = new CmsLcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.lcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetLCBValues: returning {} entries", resp.lcb.size());
        return ok(resp, reqId);
    }
}
