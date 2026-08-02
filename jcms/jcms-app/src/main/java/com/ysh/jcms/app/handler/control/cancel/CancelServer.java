package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.control.ControlCache;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.control.CmsCancelError;
import com.ysh.jcms.pdu.control.CmsCancelRequest;
import com.ysh.jcms.pdu.control.CmsCancelResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cancel server handler — 8.11.4
 *
 * <p>
 * Cancels a previous selection, releasing the lock on the control object.
 */
public class CancelServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(CancelServer.class);

    public CancelServer() {
        super(ServiceName.CANCEL, CmsCancelRequest.class, CmsCancelError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsCancelRequest req = (CmsCancelRequest) rawReq;
        String ref = str(req.reference);
        String sid = session.getSessionId();

        log.info("Cancel from {}: reqId={}, ref={}", sid, reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        if (ControlCache.release(ref, sid)) {
            log.info("Cancel: released lock '{}' for session {}", ref, sid);
            CmsCancelResponse resp = new CmsCancelResponse().reference(ref).ctlVal(req.ctlVal)
                    .operTm(req.isPresent("operTm") ? req.operTm : null).origin(req.origin).ctlNum(req.ctlNum.value()).t(req.t)
                    .test(req.test.value());
            return ok(resp, reqId);
        } else {
            return onDecodeError(reqId, CmsServiceError.CONTROL_MUST_BE_SELECTED);
        }
    }
}
