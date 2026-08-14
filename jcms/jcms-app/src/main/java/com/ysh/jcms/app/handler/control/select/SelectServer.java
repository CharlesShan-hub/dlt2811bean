package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.app.handler.control.ControlCache;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.control.CmsSelectError;
import com.ysh.jcms.core.pdu.control.CmsSelectRequest;
import com.ysh.jcms.core.pdu.control.CmsSelectResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * Select server handler — 8.11.1
 *
 * <p>
 * Locks a control object for exclusive access by the requesting session.
 */
public class SelectServer extends BaseServerHandler<CmsSelectRequest, CmsSelectError> {

    public SelectServer() {
        super(CmsServiceInfo.SELECT, CmsSelectRequest.class, CmsSelectError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSelectRequest req, int reqId) {
        String ref = str(req.reference);
        String sid = session.sessionId();

        log.info("Select from {}: reqId={}, ref={}", sid, reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String holder = ControlCache.getSelector(ref);
        if (holder != null && !holder.equals(sid))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);

        if (ControlCache.select(ref, sid)) {
            log.info("Select: locked '{}' for session {}", ref, sid);
            return ok(new CmsSelectResponse().reference(ref), reqId);
        } else {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        }
    }
}
