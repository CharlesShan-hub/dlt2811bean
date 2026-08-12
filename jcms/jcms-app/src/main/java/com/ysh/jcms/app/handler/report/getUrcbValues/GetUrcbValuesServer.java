package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsUrcb;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesRequest;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetUrcbValuesServer extends BaseServerHandler<CmsGetUrcbValuesRequest, CmsGetUrcbValuesError> {

    public GetUrcbValuesServer() {
        super(CmsServiceInfo.GET_URCB_VALUES, CmsGetUrcbValuesRequest.class, CmsGetUrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetUrcbValuesRequest req, int reqId) {
        log.info("GetURCBValues from {}: reqId={}, {} refs", session.sessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetUrcbValuesResponse resp = new CmsGetUrcbValuesResponse();

        for (int i = 0; i < req.reference.size(); i++) {
            String ref = str(req.reference.get(i));
            CmsUrcbValueChoice choice = new CmsUrcbValueChoice();
            CmsUrcb urcb = SclControlBlockService.resolveUrcb(ied, ap, ref);
            if (urcb != null) {
                // Association-scoped runtime overlay (URCB is per-association, 8.7.4)
                CmsBrcb rt = CbStateManager.ASSOCIATION.get(session.sessionId(), ref);
                if (rt != null) {
                    SclControlBlockService.overlayUrcbRuntime(urcb, rt);
                }
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
