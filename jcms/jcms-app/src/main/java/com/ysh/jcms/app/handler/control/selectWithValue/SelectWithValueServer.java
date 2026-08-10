package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.control.ControlCache;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.control.CmsSelectWithValueError;
import com.ysh.jcms.pdu.control.CmsSelectWithValueRequest;
import com.ysh.jcms.pdu.control.CmsSelectWithValueResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * SelectWithValue server handler — 8.11.2
 *
 * <p>
 * Locks a control object and echoes back the target value (ctlVal).
 */
public class SelectWithValueServer extends BaseServerHandler<CmsSelectWithValueRequest, CmsSelectWithValueError> {

    public SelectWithValueServer() {
        super(ServiceName.SELECT_WITH_VALUE, CmsSelectWithValueRequest.class, CmsSelectWithValueError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSelectWithValueRequest req, int reqId) {
        String ref = str(req.reference);
        String sid = session.sessionId();

        log.info("SelectWithValue from {}: reqId={}, ref={}", sid, reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String holder = ControlCache.getSelector(ref);
        if (holder != null && !holder.equals(sid))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);

        if (ControlCache.select(ref, sid)) {
            log.info("SelectWithValue: locked '{}' for session {}", ref, sid);
            CmsSelectWithValueResponse resp = new CmsSelectWithValueResponse().reference(ref).ctlVal(req.ctlVal)
                    .operTm(req.isPresent("operTm") ? req.operTm : null).origin(req.origin).ctlNum(req.ctlNum.value()).t(req.t)
                    .test(req.test.value()).check(req.check);
            return ok(resp, reqId);
        } else {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        }
    }
}
