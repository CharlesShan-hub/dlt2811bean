package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesError;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesRequest;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class ConfirmEditSgValuesServer extends BaseServerHandler<CmsConfirmEditSgValuesRequest, CmsConfirmEditSgValuesError> {

    public ConfirmEditSgValuesServer() {
        super(CmsServiceInfo.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSgValuesRequest.class, CmsConfirmEditSgValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsConfirmEditSgValuesRequest req, int reqId) {

        String ref = str(req.sgcbReference);
        log.info("ConfirmEditSGValues from {}: reqId={}, sgcbRef={}", session.sessionId(), reqId, ref);

        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        SgcState state = SgSessionState.getState(session.sessionId());
        int count = state.getEditValues().size();
        state.commitEditValues();

        log.info("ConfirmEditSGValues: committed {} values for sgcbRef={}, session={}", count, ref, session.sessionId());
        return ok(new CmsConfirmEditSgValuesResponse(), reqId);
    }
}
