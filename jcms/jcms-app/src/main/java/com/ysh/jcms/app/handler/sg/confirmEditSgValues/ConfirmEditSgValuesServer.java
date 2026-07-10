package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesError;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesRequest;
import com.ysh.jcms.svc.sg.CmsConfirmEditSgValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmEditSgValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfirmEditSgValuesServer.class);

    public ConfirmEditSgValuesServer() {
        super(ServiceName.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSgValuesRequest.class, CmsConfirmEditSgValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsConfirmEditSgValuesRequest req = (CmsConfirmEditSgValuesRequest) rawReq;

        String ref = str(req.sgcbReference);
        log.info("ConfirmEditSGValues from {}: reqId={}, sgcbRef={}", session.getSessionId(), reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        SgcState state = SgSessionState.getState(session.getSessionId());
        int count = state.getEditValues().size();
        state.commitEditValues();

        log.info("ConfirmEditSGValues: committed {} values for sgcbRef={}, session={}", count, ref, session.getSessionId());
        return ok(new CmsConfirmEditSgValuesResponse().reqId(reqId), reqId);
    }
}
