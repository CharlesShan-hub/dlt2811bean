package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetLcbValuesServer extends BaseServerHandler<CmsGetLcbValuesRequest, CmsGetLcbValuesError> {

    public GetLcbValuesServer() {
        super(CmsServiceInfo.GET_LCB_VALUES, CmsGetLcbValuesRequest.class, CmsGetLcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLcbValuesRequest req, int reqId) {
        log.info("GetLCBValues from {}: reqId={}, {} refs", session.sessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetLcbValuesResponse resp = new CmsGetLcbValuesResponse();

        for (CmsObjectReference refObj : req.reference) {
            String ref = str(refObj);
            CmsLcbValueChoice choice;
            // Runtime state layer first, fall back to SCL
            CmsLcb lcb = CbStateManager.LCB.get(ref);
            if (lcb == null) {
                lcb = SclControlBlockService.resolveLcb(ied, ap, ref);
            }
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
