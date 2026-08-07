package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsMsvcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesRequest;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * GetMSVCBValues server handler.
 *
 * <p>
 * Reads MSVCB control block values from SCL (or in-memory cache if previously
 * modified via SetMSVCBValues).
 */
public class GetMsvcbValuesServer extends BaseServerHandler<CmsGetMsvcbValuesRequest, CmsGetMsvcbValuesError> {

    public GetMsvcbValuesServer() {
        super(ServiceName.GET_MSVCB_VALUES, CmsGetMsvcbValuesRequest.class, CmsGetMsvcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetMsvcbValuesRequest req, int reqId) {
        log.info("GetMSVCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        CmsGetMsvcbValuesResponse resp = new CmsGetMsvcbValuesResponse();

        for (CmsObjectReference refObj : req.reference) {
            String ref = str(refObj);
            CmsMsvcbValueChoice choice;
            CmsMsvcb msvcb = SclControlBlockService.resolveMsvcb(ied, ap, ref);
            if (msvcb != null) {
                choice = new CmsMsvcbValueChoice().altValue(msvcb);
            } else {
                choice = new CmsMsvcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.msvcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetMSVCBValues: returning {} entries", resp.msvcb.size());
        return ok(resp, reqId);
    }
}
