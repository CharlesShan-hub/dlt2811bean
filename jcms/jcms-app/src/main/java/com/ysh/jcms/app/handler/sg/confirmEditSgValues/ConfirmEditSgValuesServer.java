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

import java.nio.charset.StandardCharsets;

public class ConfirmEditSgValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfirmEditSgValuesServer.class);

    public ConfirmEditSgValuesServer() {
        super(ServiceName.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSgValuesRequest.class, CmsConfirmEditSgValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsConfirmEditSgValuesRequest req = (CmsConfirmEditSgValuesRequest) rawReq;
        int reqId = req.reqId.value();

        String ref = req.sgcbReference.len > 0
            ? new String(req.sgcbReference.value(), StandardCharsets.UTF_8) : null;

        log.info("ConfirmEditSGValues from {}: reqId={}, sgcbRef={}",
            session.getSessionId(), reqId, ref);

        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // Promote edit (SE) buffer to committed (SG) layer
        SgcState state = SgSessionState.getState(session.getSessionId());
        int count = state.getEditValues().size();
        state.commitEditValues();

        log.info("ConfirmEditSGValues: committed {} values for sgcbRef={}, session={}",
            count, ref, session.getSessionId());

        try {
            CmsConfirmEditSgValuesResponse resp = new CmsConfirmEditSgValuesResponse()
                .reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode ConfirmEditSGValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
