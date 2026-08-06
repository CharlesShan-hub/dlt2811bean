package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.control.ControlCache;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.control.CmsOperateError;
import com.ysh.jcms.pdu.control.CmsOperateRequest;
import com.ysh.jcms.pdu.control.CmsOperateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * Operate server handler — 8.11.3
 *
 * <p>
 * Executes a control operation. The control object should have been previously
 * selected via Select or SelectWithValue.
 */
public class OperateServer extends BaseServerHandler {

    public OperateServer() {
        super(ServiceName.OPERATE, CmsOperateRequest.class, CmsOperateError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsOperateRequest req = (CmsOperateRequest) rawReq;
        String ref = str(req.reference);
        String sid = session.getSessionId();

        log.info("Operate from {}: reqId={}, ref={}", sid, reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        if (!ControlCache.isSelectedBy(ref, sid))
            return onDecodeError(reqId, CmsServiceError.CONTROL_MUST_BE_SELECTED);

        ControlCache.release(ref, sid);
        log.info("Operate: executed '{}' for session {}, lock released", ref, sid);

        return ok(new CmsOperateResponse().reference(ref), reqId);
    }
}
